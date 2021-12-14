import pojo.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class Salir extends HttpServlet{
	
	public void proceso (HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
	{
		response.setContentType ("text/html");
		PrintWriter out =response.getWriter();
		
		//CABECERA.......................
		RequestDispatcher rdCab = getServletContext().getRequestDispatcher("/cabecera.html");
		rdCab.include(request, response);
		
		//Variables.............................
		HttpSession miSesion = request.getSession();
		GregorianCalendar fechaInicio = (GregorianCalendar) miSesion.getAttribute("horaInicioSesion");
		String nombreUsuario = (String)miSesion.getAttribute("nombreUsuario");
		HashMap<Integer,Pedido> cestaArticulos = (HashMap<Integer,Pedido>) miSesion.getAttribute("cestaArticulos");
		Map<Integer,Articulo> articulosTienda = (Map<Integer,Articulo>) getServletContext().getAttribute("articulosTienda");
		
		//Calculamos el tiempo transcurrido desde el inicio de sesión
		long tiempoTranscurrido = ((new GregorianCalendar()).getTimeInMillis() - fechaInicio.getTimeInMillis())/1000;
		
		//Vaciamos la cesta y volvemos a aumentar el numero de unidades de la tienda
		for(int codArtCest : cestaArticulos.keySet()){
			int udCesta = cestaArticulos.get(codArtCest).getUdPedidas();
			articulosTienda.get(codArtCest).incrementarExist(udCesta);
		}
		
		out.println("<h1>Gracias por contar con nosotros, "+ nombreUsuario +"</h1>");
		out.println("<h2>Tiempo transcurrido en la aplicación: "+ tiempoTranscurrido +" segundos.</h2>");
		out.println("</table><a href='index.html'>Volver a inicio</a>");
		
		//Cerramos la sesión
		miSesion.invalidate();
		
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