import pojo.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class VerCesta extends HttpServlet{
	
	public void proceso (HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
	{
		response.setContentType ("text/html");
		PrintWriter out =response.getWriter();
		
		//CABECERA.......................
		RequestDispatcher rdCab = getServletContext().getRequestDispatcher("/cabecera.html");
		rdCab.include(request, response);
		
		//Variables......................
		HttpSession miSesion = request.getSession();
		HashMap<Integer,Pedido> cestaArticulos = (HashMap<Integer,Pedido>) miSesion.getAttribute("cestaArticulos");
		Map<Integer,Articulo> articulosTienda = (Map<Integer,Articulo>) getServletContext().getAttribute("articulosTienda");
		String nombreUsuario = (String)miSesion.getAttribute("nombreUsuario");
		
		//Comprobamos que el mensaje no es nulo(si la llamada viene desde listaArt)
		String msjAccionRealizada = (String)request.getAttribute("msjAccion");
		if(msjAccionRealizada == null){
			msjAccionRealizada ="";
		}

		//Mostramos los artículos comprados en una tabla
		out.println("<h1>"+ nombreUsuario +", tus artículos en la cesta hasta ahora son: </h1>");
		out.println("<table class='tCesta'><tr><th colspan='2'>Artículo</th><th>Uds Pedidas</th><th>Precio Total<th></tr>");
		Articulo art;
		if(cestaArticulos.size() > 0){
			for(Integer codArt : cestaArticulos.keySet()){
				art = articulosTienda.get(codArt);
				out.println("<form action='VerCesta' method='POST'>");
				out.println("<tr><td><img src='img/"+art.getNombre()+".png'></td><td>"+ art.getNombre() +"</td>");
				out.println("<td>"+ cestaArticulos.get(codArt).getUdPedidas() +"</td>");
				out.println("<td>"+ cestaArticulos.get(codArt).getPrecioTotal() +"$</td>");
				out.println("<input type='hidden' name='artCancelado' value='"+codArt+"'>");
				out.println("<td><button type='submit' name='op' value='borrar'><i class='fontawesome-remove-sign'></i></button>");
				out.println("<button type='submit' name='op' value='añadir uno'><i class='fontawesome-circle-arrow-up'></i></button>");
				out.println("<button type='submit' name='op' value='quitar'><i class='fontawesome-circle-arrow-down'></i></button></td></tr>");
				out.println("</form>");
			}

		}else{
			out.println("<tr><td>Aún no hay artículos en la cesta.</td></tr>");
		}
		out.println("</table>");
		out.println("<p>"+msjAccionRealizada+"</p>");
		out.println("<form action='ConfirmarPedido'><input type='submit' value='Confirmar Pedido'></form>");  
		out.println("<form action='ListaArt'><input type='submit' value='Seguir Comprando'></form>");
		out.println("<form action='Salir'><input type='submit' value='Salir sin comprar'></form>");
		
		//PIE DE PAGINA.......................
		RequestDispatcher rdPie = getServletContext().getRequestDispatcher("/pie.html");
		rdPie.include(request, response);	
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
		proceso(request,response);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
		RequestDispatcher rdPie = getServletContext().getRequestDispatcher("/GestionCarrito");
		rdPie.include(request, response);	
		proceso(request,response);
	}
}