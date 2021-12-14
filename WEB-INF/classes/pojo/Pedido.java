package pojo;

public class Pedido{
	private int codigoArt;
	private int udPedidas;
	private double precioTotal;
	
	public Pedido(){}
	
	public Pedido(int codigoArt, int udPedidas, double precioTotal){
		this.codigoArt = codigoArt;
		this.udPedidas = udPedidas;
		this.precioTotal = precioTotal;
	}
	
	public int getUdPedidas(){
		return this.udPedidas;
	}
	
	public int getCodigoArt(){
		return this.codigoArt;
	}
	
	public double getPrecioTotal(){
		return this.precioTotal;
	}
	
	public void aumentarPrecio(double precioExtra){
		this.precioTotal += precioExtra;
	}
	
	public void setUdPedidas(int cantidadPedida){
		this.udPedidas = cantidadPedida;
	}
	
	public void incrUdPedidas(int nuevasUd){
		this.udPedidas += nuevasUd;
	}
	
	public void decremUdPedidas(int nuevasUd){
		this.udPedidas -= nuevasUd;
	}
}