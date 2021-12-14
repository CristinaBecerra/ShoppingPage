import pojo.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class ConfirmPedido extends HttpServlet{
	
	public void proceso (HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
	{
		response.setContentType ("text/html");
		PrintWriter out =response.getWriter();
		
		//CABECERA................................................................................
		RequestDispatcher rdCab = getServletContext().getRequestDispatcher("/cabecera.html");
		rdCab.include(request, response);
		
		//Variables......................................................................................
		HttpSession miSesion = request.getSession();
		String nombreUsuario = (String)miSesion.getAttribute("nombreUsuario");
		int numTicket = (Integer)getServletContext().getAttribute("numTicket");
		HashMap<Integer,Pedido> cestaArticulos = (HashMap<Integer,Pedido>) miSesion.getAttribute("cestaArticulos");
		Map<Integer,Articulo> articulosTienda = (Map<Integer,Articulo>) getServletContext().getAttribute("articulosTienda");
		double totalTicket = 0;
		
		//Pintamos el ticket al usuario................................................................................
		out.println("<h1>Gracias por comprar con nosotros "+ nombreUsuario +"</h1>");
		out.println("<h2>Este es su ticket de compra (nº"+ numTicket +"):</h2>");
		out.println("<table id='tableTicket'><tr><th>Articulo</th><th>Num.Ud</th><th>Pvp</th><th>Precio total</th></tr>");
		if(cestaArticulos.size() > 0){
			for(Integer codArt : cestaArticulos.keySet()){
				Articulo art = articulosTienda.get(codArt);
				Pedido ped = cestaArticulos.get(codArt);
				out.println("<tr><td>"+ art.getNombre() +"</td><td>"+ ped.getUdPedidas()+"</td>");
				out.println("<td>"+ art.getPrecio() +"$</td><td>"+ ped.getPrecioTotal() +"$</td></tr>");
				totalTicket += ped.getPrecioTotal();
			}
		}else{
			out.println("<tr><td>Aún no hay artículos en la cesta.</td></tr>");
		}
		
		//Pintamos los precios finales calculando el iva................................................................
		DecimalFormat df = new DecimalFormat("#####0.00");
		double iva = (totalTicket*21)/100;
		out.println("<tr><td colspan='2'>Total Ticket: </td><td colspan='2'>"+ df.format(totalTicket) +"$</td></tr>");
		out.println("<tr><td colspan='2'>IVA(21%) </td><td colspan='2'>"+ df.format(iva) +"$</td></tr>");
		out.println("<tr><td colspan='2'>Total con IVA: </td><td colspan='2'>"+ df.format(totalTicket+iva) +"$</td></tr>");
		out.println("</table>");
		
		//Actualizamos el número de ticket
		numTicket++;
		getServletContext().setAttribute("numTicket", numTicket);
		
		//Cerramos la sesión
		miSesion.invalidate();
		
		out.println("<a href='index.html'>Volver a inicio</a>");
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
		proceso(request,response);
	}
}