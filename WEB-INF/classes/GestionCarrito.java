import pojo.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class GestionCarrito extends HttpServlet{
	
	public void proceso (HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
	{
		//Variables..................................
		HashMap<Integer,Pedido> cestaArticulos = (HashMap<Integer,Pedido>) request.getSession().getAttribute("cestaArticulos");
		Map<Integer,Articulo> articulosTienda = (Map<Integer,Articulo>) getServletContext().getAttribute("articulosTienda");
		PrintWriter out =response.getWriter();
		String msjAccionRealizada = "";
	
		//Comprobamos si el servlet viene desde ListaArt....................................................
		if(request.getParameter("codArtSelec") != null){
			int codigoArtSelec = Integer.parseInt(request.getParameter("codArtSelec"));	
			Articulo art = articulosTienda.get(codigoArtSelec);
			int existenciasArt = art.getExistencias();
			
			//Para hacer el casting, el campo de cantidad de articulos no puede estar vacío
			int cantArtSelec=0;
			if(request.getParameter("cantArticulo") != ""){
				cantArtSelec = Integer.parseInt(request.getParameter("cantArticulo"));
			}
			
			//Actualizamos la cesta y las unidades restantes en stock.....................................................
			if(existenciasArt > 0){
				if(cantArtSelec > 0){
					double precioUd = art.getPrecio();
					//Ci la cesta ya contiene el articulo aumentamos ud pedidas
					if(cestaArticulos.containsKey(codigoArtSelec)){ 
						cestaArticulos.get(codigoArtSelec).incrUdPedidas(cantArtSelec);
						cestaArticulos.get(codigoArtSelec).aumentarPrecio(precioUd*cantArtSelec);
					}else{
						double precioTotal = precioUd*cantArtSelec;
						cestaArticulos.put(codigoArtSelec, new Pedido(codigoArtSelec,cantArtSelec, precioTotal));
					}
					
					//Decrementamos existencias del mapa de la tienda
					art.decrementarExist(cantArtSelec);
					msjAccionRealizada = "Artículo "+ art.getNombre() +" añadido con éxito a la cesta.";
				}else{
					msjAccionRealizada = "No se pueden seleccionar 0 artículos.";
				}
			}else{
				msjAccionRealizada = "Se ha agotado el stock del artículo seleccionado.";
			}
	
			
		//......SI..VIENE..DESDE..VERCESTA...................................................................................
		}else{
			String valorSubmit = request.getParameter("op");
			int codArt = Integer.parseInt(request.getParameter("artCancelado")); 
			Articulo art = articulosTienda.get(codArt);
			
			if(valorSubmit.equals("borrar")){
				art.incrementarExist(cestaArticulos.get(codArt).getUdPedidas());
				cestaArticulos.remove(codArt);
			}else if(valorSubmit.equals("añadir uno")){
				//Comprobamos si hay existencias del artículo
				if(art.getExistencias() > 0){
					art.decrementarExist(1);
					cestaArticulos.get(codArt).incrUdPedidas(1);
				}else{
					msjAccionRealizada = "No hay más unidades en stock de " + art.getNombre() + ".";
				}
			
			}else{ //Aquí desde decrementar unidad
				cestaArticulos.get(codArt).decremUdPedidas(1);
				art.incrementarExist(1);
				//Si al decrementar uds en la cesta es 0, que se quite de la cesta
				if(cestaArticulos.get(codArt).getUdPedidas() == 0){
					cestaArticulos.remove(codArt);
				}
			}
			
		}
		
		//Añadimos el mensaje según la acción que se haya producido
		request.setAttribute("msjAccion", msjAccionRealizada);
	}
	
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
		proceso(request,response);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
		proceso(request,response);
	}
}