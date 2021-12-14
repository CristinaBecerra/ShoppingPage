import pojo.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class ListaArt extends HttpServlet{
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//Montamos el mapa con los artículos de la tienda
		Map<Integer,Articulo> articulosTienda = new HashMap();
		String[] articulosIniciales = getServletContext().getInitParameter("articulos").split(";");
		String[] parametrosArt;
		
		for(int i = 0; i < articulosIniciales.length; i++){
			parametrosArt = articulosIniciales[i].split(",");
			articulosTienda.put(i+1,new Articulo(i+1,parametrosArt[0],Integer.parseInt(parametrosArt[1]), Double.parseDouble(parametrosArt[2])));
		}
		
		getServletContext().setAttribute("articulosTienda", articulosTienda);
		getServletContext().setAttribute("numTicket", new Integer(1));
	}
	
	public void proceso (HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
	{
		response.setContentType ("text/html");
		PrintWriter out =response.getWriter();
		
		//CABECERA.......................
		RequestDispatcher rdCab = getServletContext().getRequestDispatcher("/cabecera.html");
		rdCab.include(request, response);
	
		
		HttpSession miSesion = request.getSession();
		//Añadimos la hora y creamos cesta si la sesión es nueva
		if(miSesion.isNew()){
			miSesion.setAttribute("horaInicioSesion", new GregorianCalendar());
			miSesion.setAttribute("cestaArticulos", new HashMap<Integer,Pedido>());
			miSesion.setAttribute("nombreUsuario", request.getParameter("nombreUsu"));
		}
		
		//Variables...........................
		Map<Integer,Articulo> articulosTienda = (Map<Integer,Articulo>) getServletContext().getAttribute("articulosTienda");
		GregorianCalendar fechaInicioSes = (GregorianCalendar) miSesion.getAttribute("horaInicioSesion");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		String msjAccionRealizada = (String)request.getAttribute("msjAccion");
		String nombreUsuario = (String)miSesion.getAttribute("nombreUsuario"); 
		
		//Comprobamos si la llamada viene desde AddArticulo o desde index
		if(msjAccionRealizada == null){
			msjAccionRealizada = "Artículos disponibles";
		}
		
		
		out.println("<h1>Bienvenido/a " + nombreUsuario +", has conectado a las " + sdf.format(fechaInicioSes.getTime())+"</h1>");
		out.println("<h1>¿Qué quiere comprar?</h1>");
		out.println("<p class='msjAccion'>"+ msjAccionRealizada +"</p>");
		
		//Pintamos todos los artículos y su número de ud disponibles
		String msjUnidades;
		String disabled;
		out.println("<table><tr><th colspan='2'><th>Stock</th></th><th>Unidades a comprar</th></tr>");
		for(Articulo art : articulosTienda.values()){
			int numUnidades = art.getExistencias();
			if(numUnidades>0){
				disabled = "";
				msjUnidades = "Unidades en stock: <b>" + numUnidades + "</b>";
			}else{
				disabled = "disabled";
				msjUnidades = "No hay unidades en stock.";
			}
			out.println("<form action='ListaArt' method='POST'>");
			out.println("<tr><td><label>"+ art.getNombre() +"</label></td>");
			out.println("<td><img src='img/"+ art.getImagen() +"'></img></td>");
			out.println("<td class='msjUnidades'>"+ msjUnidades +"</td>");
			out.println("<td class='msjUnidades'><input type='number' min=0 max="+numUnidades+" name='cantArticulo'></td>");
			out.println("<td><p><b>Precio: </b>"+ art.getPrecio() +"</p></td>");
			out.println("<input type='hidden' name='codArtSelec' value='"+ art.getCodigo() +"'>");
			out.println("<td><input type='image' src='img/carrito.png' width='20' height='20'"+ disabled +"></td></tr>");
			out.println("</form>");
		}
		
		out.println("</table>");
		out.println("<form action='VerCesta'><input type='submit' value='Ver Cesta'></form>");
		out.println("<form action='Salir'><input type='submit' value='Cerrar Sesión'></form>");
		
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