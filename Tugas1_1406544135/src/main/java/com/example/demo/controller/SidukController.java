package com.example.demo.controller;


import java.text.SimpleDateFormat; 
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.KecamatanModel;
import com.example.demo.model.KeluargaModel;
import com.example.demo.model.KelurahanModel;
import com.example.demo.model.KotaModel;
import com.example.demo.model.PendudukModel;
import com.example.demo.service.SidukService;

@Controller
public class SidukController
{
    @Autowired
    SidukService sidukDAO;
    @RequestMapping("/")
    public String index ()
    {
        return "index";
    }

    @RequestMapping(value= "/penduduk", method = RequestMethod.GET)
    public String detailPenduduk (Model model,
            @RequestParam(value = "nik", required=false) String nik)
    {
    	if(nik==null){
    		return "error-page";
    	}
    	 PendudukModel penduduk = sidukDAO.selectPenduduk(nik);
    
    	 if(penduduk!= null){
	    	 model.addAttribute ("penduduk", penduduk);
	        return "detail-penduduk";
	      }
    	 else{
	    	 model.addAttribute("nomor",nik);
	    	 return "not-found";	
    	 }
    }
    
    @RequestMapping(value="/penduduk/tambah", method = RequestMethod.GET)
    public String addPenduduk (Model model, PendudukModel penduduk)
    {
        return "form-tambah-penduduk";
    }
    
    @RequestMapping(value= "/penduduk/tambah", method = RequestMethod.POST)
    public String addPendudukSubmit (Model model, @Valid PendudukModel penduduk, BindingResult bindingResult)
    {
    	if(bindingResult.hasErrors()) {
    		return "form-tambah-penduduk";
    	} else {
    		KeluargaModel keluarga = sidukDAO.selectKeluarga(penduduk.getId_keluarga());
    		String kodeKecamatan = keluarga.getKelurahan().getKecamatan().getKode_kecamatan().substring(0, 6);
    		
    		Date ymd = null;
    		try {
    			ymd = new SimpleDateFormat("yyyy-MM-dd").parse(penduduk.getTanggal_lahir());			
    		} catch (Exception e) {
    			
    		}
    		
    		String tglLahir = new SimpleDateFormat("dd-MM-yyyy").format(ymd);		
    		String nikTglLahir = tglLahir.replace("-", "");
    		nikTglLahir = nikTglLahir.substring(0,4) + nikTglLahir.substring(6,8);
    		
    		if(penduduk.getJenis_kelamin() == 1){
    			Integer tanggal = Integer.parseInt(nikTglLahir.substring(0,2));
    			tanggal +=40;
    			nikTglLahir = String.valueOf(tanggal) + nikTglLahir.substring(2,6);
    		}
    		
    		String nikMin= kodeKecamatan + nikTglLahir + "0001";
    		String nikMax= kodeKecamatan + nikTglLahir + "9999";
    		PendudukModel cekPenduduk = sidukDAO.selectPendudukTerakhir(nikMin, nikMax);
    		
    		if(cekPenduduk == null){
    			penduduk.setNik(nikMin);
			sidukDAO.addPenduduk(penduduk);
    			model.addAttribute("nomor", nikMin);
    		}else{
    			Long nikBaru = Long.parseLong(cekPenduduk.getNik());
    			String nikPendudukBaru = String.valueOf(nikBaru+1);
    			penduduk.setNik(nikPendudukBaru);
			sidukDAO.addPenduduk(penduduk);
    			model.addAttribute("nomor", nikPendudukBaru);
    		}
    		return "sukses-tambah";
    	}
    }
    
    @RequestMapping(value= "/penduduk/ubah/{nik}", method = RequestMethod.GET)
    public String updatePenduduk (Model model, @PathVariable(value = "nik") String nik)
    {
        PendudukModel penduduk = sidukDAO.selectPenduduk(nik);
        
        if(penduduk==null){
        	model.addAttribute("nomor",nik);
        	return "not-found";
        }else{
        	model.addAttribute("pendudukModel", penduduk);
        }
        
    	return "form-update-penduduk";
    }
    
    @RequestMapping(value= "/penduduk/ubah/{nik}", method = RequestMethod.POST)
    public String updatePendudukSubmit (@PathVariable(value="nik") String nik, Model model, @Valid PendudukModel penduduk, BindingResult bindingResult)
    {
    	PendudukModel archive = sidukDAO.selectPenduduk(nik);
    	if(bindingResult.hasErrors()) {
    		return "form-update-penduduk";
    	} else {
    		penduduk.setId(archive.getId());
    		sidukDAO.updatePenduduk(penduduk);
        	model.addAttribute("nomor", penduduk.getNik());
    	}
	   	return "sukses-update";
   }
    
    @RequestMapping(value = "/keluarga",method = RequestMethod.GET)
    public String anggotaKeluarga(Model model,
    		@RequestParam(value="nkk") String nkk){
    	
    	KeluargaModel keluarga = sidukDAO.selectKartuKeluarga(nkk); 
    	
    	 if(keluarga!= null){
    		List<PendudukModel> anggota = sidukDAO.selectAnggotaKeluarga(nkk);
    		model.addAttribute("anggota", anggota);
    		model.addAttribute("keluarga", keluarga);
    		return "detail-keluarga";
	      }
    	 else{
	    	 model.addAttribute("nomor",nkk);
	    	 return "not-found";	
    	 }
   }
    
    @RequestMapping(value="/keluarga/tambah", method = RequestMethod.GET)
    public String addKeluarga (Model model, KeluargaModel keluarga)
    {
    	List<KotaModel> daftarKota = sidukDAO.selectDaftarKota();
    	List<KecamatanModel> daftarKecamatan = sidukDAO.selectDaftarKecamatan();
    	List<KelurahanModel> daftarKelurahan = sidukDAO.selectDaftarKelurahan();
    	
    	model.addAttribute("daftarKota", daftarKota);
    	model.addAttribute("daftarKecamatan", daftarKecamatan);
    	model.addAttribute("daftarKelurahan", daftarKelurahan);
        return "form-tambah-keluarga";
    }
        
    @RequestMapping(value= "/keluarga/tambah", method = RequestMethod.POST)
    public String addKeluargaSubmit (Model model, @Valid KeluargaModel keluarga, BindingResult bindingResult,
    		@RequestParam(value = "kota") Integer idKota,
    		@RequestParam(value = "kecamatan") Integer idKecamatan,
    		@RequestParam(value = "kelurahan") Integer idKelurahan){
    	
    	Date date = new Date();
		String currentDate  = new SimpleDateFormat("dd-MM-yyyy").format(date);
		currentDate= currentDate.replace("-", "");
		currentDate= currentDate.substring(0,4) + currentDate.substring(6,8);
		KelurahanModel kelurahan = sidukDAO.selectKelurahan(idKelurahan);
		String kodeKecamatan = kelurahan.getKecamatan().getKode_kecamatan().substring(0, 6);
		
		
		String nkkMin= kodeKecamatan + currentDate + "0001";
		String nkkMax= kodeKecamatan + currentDate + "9999";
		KeluargaModel cekKeluarga = sidukDAO.selectKeluargaTerakhir(nkkMin, nkkMax);
		
		if(cekKeluarga == null){
			keluarga.setNomor_kk(nkkMin);
			model.addAttribute("nomor", nkkMin);
		}else{
			Long nkkBaru = Long.parseLong(cekKeluarga.getNomor_kk());
			String nkkKeluargaBaru = String.valueOf(nkkBaru+1);
			keluarga.setNomor_kk(nkkKeluargaBaru);
			model.addAttribute("nomor", nkkKeluargaBaru);
		}
	
		keluarga.setId_kelurahan(idKelurahan);
		sidukDAO.addKeluarga(keluarga);
    	return "sukses-tambah";

    }
}
