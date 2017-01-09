package com.grupohi.almacenv1.modelo;

public class ListviewGenerico {
    private String descripcion;
	private String unidad;
	private Double existencia;
	private int idmaterial;
	private int idobra;
	private int idalmacen;
	private String nombrealmacen;
	private String fechahora;
	private int idcontratista, concargo;
	private String nombrecontratista;

    public ListviewGenerico (int idmaterial, String descripcion, String unidad, Double existencia, int idobra, int idalmacen, String nombrealmacen, int idcontratista, int concargo, String nombrecontratista) {
        this.idmaterial=idmaterial;
    	this.descripcion = descripcion;
        this.unidad=unidad;
        this.existencia=existencia;
        this.idobra=idobra;
        this.idalmacen=idalmacen;
        this.nombrealmacen=nombrealmacen;
        this.idcontratista=idcontratista;
        this.concargo=concargo;
        this.nombrecontratista=nombrecontratista;
    }
    public ListviewGenerico (int idmaterial, String descripcion, String unidad, Double existencia, int idobra, int idalmacen, String nombrealmacen, String fechahora) {
        this.idmaterial=idmaterial;
    	this.descripcion = descripcion;
        this.unidad=unidad;
        this.existencia=existencia;
        this.idobra=idobra;
        this.idalmacen=idalmacen;
        this.nombrealmacen=nombrealmacen;
        this.fechahora=fechahora;

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
    public int getidobra() {
        return this.idobra;
    }
    public int getidalmacen() {
        return this.idalmacen;
    }
    public String getnombrealmacne() {
        return this.nombrealmacen;
    }
    public String getfechahora() {
        return this.fechahora;
    }
    public int getidcontratista() {
        return this.idcontratista;
    }
    public int getconcargo() {
        return this.concargo;
    }
    public String getnombrecontratista() {
        return this.nombrecontratista;
    }

}
