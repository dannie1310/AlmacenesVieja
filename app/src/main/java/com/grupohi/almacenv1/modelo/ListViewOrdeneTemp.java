package com.grupohi.almacenv1.modelo;

import com.grupohi.almacenv1.lib.*;

public class ListViewOrdeneTemp {
	private String claveconcepto;
	private String unidad;
	private int idalmacen;
	private Double existencia;
	private int idmaterial;
	private String descripcion_material;
	private String descripcion_almacen;
	private int idtransaccion;
	private int folio;
	private String foliomobil;
	private String remision;
	private String observaciones;
	private String nombrecontratista;
	private int idcontratista;
	private int concargo;
	private int id_item;

	public ListViewOrdeneTemp( 
					int idmaterial, 
					String foliomobil, 
					Double existencia, 
					int idalmacen, 
					String clave_concepto, 
					String descripcion_m, 
					String descripcion_al, 
					int indtrans, 
					int folio, 
					String remision, 
					String observaciones, 
					int idcontratista, 
					String nombrecontratista, 
					int concargo, 
					int id_item, String unidad) {		
		this.idmaterial=idmaterial;
		this.unidad = unidad;
		this.existencia = existencia;
		this.idalmacen =idalmacen;
		this.claveconcepto = clave_concepto;
		this.descripcion_material=descripcion_m;
		this.descripcion_almacen=descripcion_al;
		this.idtransaccion=indtrans;
		this.foliomobil=foliomobil;
		this.folio=folio;
		this.remision=remision;
		this.observaciones=observaciones;
		this.nombrecontratista=nombrecontratista;
		this.idcontratista=idcontratista;
		this.concargo=concargo;
		this.id_item=id_item;
		
	}
	public int getId_Item() {
		return this.id_item;
	}
	public int getIdContratista() {
		return this.idcontratista;
	}
	public int getconcargo() {
		return this.concargo;
	}
	public String getNombreContratista() {
		return this.nombrecontratista;
	}

	public String getclaveconcepto() {
		return this.claveconcepto;
	}
	public String getdescripcion_material() {
		return this.descripcion_material;
	}
	public String getdescripcionalmacen() {
		return this.descripcion_almacen;
	}
	public String getunidad() {
		return this.unidad;
	}

	public Double getexistencia() {
		return this.existencia;
	}
	public int getidalmacen() {
		return this.idalmacen;
	}
	public int getidmaterial() {
		return this.idmaterial;
	}
	public int gettrasaccion() {
		return this.idtransaccion;
	}
	public int getfolio() {
		return this.folio;
	}
	public String getfoliomobil() {
		return this.foliomobil;
	}
	public String getremision() {
		return this.remision;
	}
	public String getObservaciones() {
		return this.observaciones;
	}
	public void setremision(String x) {
		 this.remision=x;
	}
	public void setObservaciones(String x) {
		this.observaciones=x;
	}
	

}
