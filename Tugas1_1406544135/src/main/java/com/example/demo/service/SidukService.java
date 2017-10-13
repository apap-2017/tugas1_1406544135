package com.example.demo.service;

import java.util.List;

import com.example.demo.model.KecamatanModel;
import com.example.demo.model.KeluargaModel;
import com.example.demo.model.KelurahanModel;
import com.example.demo.model.KotaModel;
import com.example.demo.model.PendudukModel; 

public interface SidukService
{
	PendudukModel selectPenduduk(String nik);
	KeluargaModel selectKeluarga(Integer id_keluarga);
	List<PendudukModel> selectAnggotaKeluarga(String nkk);
	List<KotaModel> selectDaftarKota();
	List<KecamatanModel> selectDaftarKecamatan();
	List<KelurahanModel> selectDaftarKelurahan();
	KeluargaModel selectKartuKeluarga(String nkk);
	PendudukModel selectPendudukTerakhir(String nikMin, String nikMax);
	KeluargaModel selectKeluargaTerakhir(String nkkMin, String nkkMax);
	KelurahanModel selectKelurahan(Integer id_kelurahan);
	void addPenduduk(PendudukModel penduduk);
	void updatePenduduk(PendudukModel penduduk);
	void addKeluarga(KeluargaModel keluarga);
}
