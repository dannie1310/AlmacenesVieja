package com.grupohi.almacenv1.modelo;

import android.util.Log;

public class ListViewOrdenesCompra {

    private String descripcion;
	private String unidad;
	private Double existencia;
	private int idmaterial;
	private int idtransaccion;
	private int folio;
	private int id_item;

    public ListViewOrdenesCompra (int idmaterial, String descripcion, String unidad, Double existencia, int trans, int foliox, int id_item) {
        this.idmaterial=idmaterial;
    	this.descripcion = descripcion;
        this.unidad=unidad;
        this.existencia=existencia;
        this.idtransaccion=trans;
        this.folio=foliox;
        this.id_item=id_item;

    }
    public void setexistencia(Double existencia) {
    	this.existencia=existencia;
	}

    public String getdescripcion() {
        return  this.descripcion;
    }
    public String getunidad() {
        return this.unidad;
    }
    public Double getexistencia() {
        return this.existencia;
    }
    public int getidmaterial() {
        return this.idmaterial;
    }
    public int getidtransacion() {
        return this.idtransaccion;
    }
    public int getfolio() {
        return this.folio;
    }
    public int getId_item() {
        return this.id_item;
    }

}

