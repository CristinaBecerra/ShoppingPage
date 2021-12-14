package pojo;

public class Articulo{
	private String nombre;
	private int codigo;
	private String imagen;
	private int existencias;
	private double precio;
	
	public Articulo(){}
	
	public Articulo(int codigo, String nombre, int existencias, double precio){
		this.nombre = nombre;
		this.codigo = codigo;
		this.existencias = existencias;
		this.precio = precio;
		this.imagen = nombre + ".png";
	}
	
	public String getNombre(){
		return this.nombre;
	}
	
	public int getExistencias(){
		return this.existencias;
	}
	
	public int getCodigo(){
		return this.codigo;
	}
	
	public String getImagen(){
		return this.imagen;
	}
	
	public double getPrecio(){
		return this.precio;
	}
	
	public void decrementarExist(int cantidadVendida){
		this.existencias -= cantidadVendida;
	}
	
	public void incrementarExist(int cantidadVendida){
		this.existencias += cantidadVendida;
	}
	
}