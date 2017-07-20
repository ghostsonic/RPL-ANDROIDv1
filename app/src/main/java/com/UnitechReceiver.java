package com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.lachewendy.rpl_androidv1.ConteoCiclicoActivity;
import com.example.lachewendy.rpl_androidv1.DarEntradaActivity;
import com.example.lachewendy.rpl_androidv1.RecoleccionActivity;
import com.example.lachewendy.rpl_androidv1.RecoleccionUbicacionActivity;
import com.example.lachewendy.rpl_androidv1.SalidaActivity;
import com.example.lachewendy.rpl_androidv1.UbicacionActivity;
import com.uny2.clases.Entrada;
import com.uny2.clases.Recoleccion;

import java.util.ArrayList;

public class UnitechReceiver extends BroadcastReceiver {
	EditText codigo,edcantidad;
	String ubicacion = "";
	String ubicacionDos = "";
	String codigoProveedor = "";
	boolean isValid;
	@Override
    public void onReceive(Context context, Intent intent) {
		isValid = false;
		System.out.println("Saludos->"+intent.getAction());
		if  ("unitech.scanservice.data" .equals(intent.getAction()))
		if  ("unitech.scanservice.data" .equals(intent.getAction()))
	    {
			System.out.println("Intent->"+intent.getAction());
			Bundle bundle = intent.getExtras();
	        if  (bundle != null )
	        {
	            String text = bundle.getString("text");
				System.out.println("Querecibo?->"+text);
				if(text.equalsIgnoreCase("Seleccion refaccion") || text.equalsIgnoreCase("Seleccione refacción")){
					System.out.println("Isvalido->"+isValid);
					isValid = true;
					System.out.println("Essvalido"+isValid);
				}
				try{
				 ubicacion = bundle.getString("ubicacion");
					ubicacionDos = bundle.getString("ubicacionC");
					System.out.println("ubicionca+-"+ubicacion);
				}catch (Exception e){
					e.printStackTrace();
				}try{
				codigoProveedor = bundle.getString("proveedor");
					System.out.println("ubicionca+-"+codigoProveedor);
				}catch (Exception e){
					e.printStackTrace();
				}
				String activity = bundle.getString("activity");
				System.out.println("Activity->"+activity);
				if(activity.equalsIgnoreCase("conteo")) {
					ConteoCiclicoActivity inst = ConteoCiclicoActivity.instance();
					if (inst != null)
						codigo = inst.getTexCodigoRefaccion();
					edcantidad = inst.getTexCantidadRef();
					String otroCodigo=  codigo.getHint().toString().trim() + "," + ubicacion.trim();
					System.out.println("CodigoPreUbciacion-"+otroCodigo);
					System.out.println("Ubicacion->"+  codigo.getText().toString().equalsIgnoreCase(ubicacion.trim()) + "ubicacion"+ubicacion + "codigo"+codigo.getText().toString() + "otrOCodigo"+otroCodigo);
					//if (codigo.getHint().toString().trim().equals(text.trim()) || codigo.getText().toString().equalsIgnoreCase(ubicacion.trim())) {
						System.out.println("Comparando ---->>" + text + "hint:" + codigo.getHint());
						if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {
							System.out.println("entro");
							inst.setTextCodigoRefaccion("");
							inst.setTextCantidad(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
						} else {
							System.out.println("Set en 0.0");
							inst.setTextCantidad("1.0");
							inst.setTextCodigoRefaccion("");
						}
					//}/*else{
						//inst.setMensaje("Código Erróneo", "El código "+text+" no pertenece al mismo producto");
						//inst.errorCodigo(text);
					//}*/
				}

				if(activity.equalsIgnoreCase("recoleccion")){
					//RecoleccionActivity inst = RecoleccionActivity.instance();
					RecoleccionUbicacionActivity inst = RecoleccionUbicacionActivity.instance();
					inst.focus();
					if (inst != null)
						codigo = inst.getCodigoRecoleccion();
					edcantidad = inst.getCantidadRecoleccion();
					String codigoBueno = codigo.getHint() + ","+ ubicacion;
					String codigoDos = codigo.getHint()+","+ubicacionDos;
					System.out.println("codigoBueno->"+codigoBueno + "Codigo+"+codigo + "Ubicaicon->"+ubicacion + "TextTrim"+ text.trim());
					if (codigo.getHint().toString().trim().equals(text.trim()) || codigoBueno.trim().equalsIgnoreCase(text.trim())|| codigoDos.trim().equalsIgnoreCase(text.trim())) {
						System.out.println("Comparando ---->>" + text + "hint:" + codigo.getHint());
						if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {
							System.out.println("entro");
							inst.setCodigoRecoleccion("");
							inst.setCantidadRecoleccion(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
						} else {
							System.out.println("Set en 0.0");
							inst.setCantidadRecoleccion("1.0");
							inst.setCodigoRecoleccion("");
						}
					}
				}


				if(activity.equalsIgnoreCase("buscarRecoleccionPrevia")){
					RecoleccionUbicacionActivity inst = RecoleccionUbicacionActivity.instance();
					codigo = inst.getCodigPrevioBuscador();
					System.out.println("CodigoEntre->"+codigo.getText().toString()+"->");
					ArrayList<Recoleccion> arrayList = inst.getPrevioRecoleccionArrayList();
					boolean bandera = false;
					for (int i = 0; i < arrayList.size() ; i++) {
						System.out.println("Arraylist->"+arrayList.get(i).getSku() +"UbicacionC-" +
								 codigo.getText().toString().equalsIgnoreCase(arrayList.get(i).getSku()+","+arrayList.get(i).getUbicacionC() +"-UbicacionP->"
										 +codigo.getText().toString().equalsIgnoreCase(arrayList.get(i).getSku()+","+arrayList.get(i).getUbicacionP())) );
						if(arrayList.get(i).getSku().trim().equalsIgnoreCase(codigo.getText().toString().trim())
								|| codigo.getText().toString().trim().equalsIgnoreCase(arrayList.get(i).getSku().trim()+","+arrayList.get(i).getUbicacionP().trim())
								|| codigo.getText().toString().trim().equalsIgnoreCase(arrayList.get(i).getSku().trim()+","+arrayList.get(i).getUbicacionC().trim())
								//|| codigo.getText().toString().trim().equalsIgnoreCase(arrayList.get(i).getUbicacion().trim())
								|| codigo.getText().toString().trim().equalsIgnoreCase(arrayList.get(i).getUbicacionC().trim())
								|| codigo.getText().toString().trim().equalsIgnoreCase(arrayList.get(i).getUbicacionP().trim())){
							inst.refreshRecoleccionList(i);
							System.out.println("Codigo->" + codigo.getText().toString());
							System.out.println("SKU->"+codigo.getText().toString());
							bandera = true;
						}
					}

					if(!bandera){
						inst.noEncontrada();
					}

				}

				if (activity.equalsIgnoreCase("buscadorEntrada")){
					DarEntradaActivity inst = DarEntradaActivity.instance();

						codigo = inst.getCodigoBuscador();
						ArrayList<Entrada> arrayList = inst.getArrayEntradaBuscar();
						boolean bandera = false;
						for (int i = 0; i < arrayList.size(); i++) {
							System.out.println("Arraylist->"+arrayList.get(i).getSkuADO()+"UbicacionC-" +
									codigo.getText().toString().equalsIgnoreCase(arrayList.get(i).getSkuADO()));
							if(arrayList.get(i).getSkuADO().trim().equalsIgnoreCase(codigo.getText().toString().trim())
									|| arrayList.get(i).getSkuProveedor().trim().equalsIgnoreCase(codigo.getText().toString().trim())){
								inst.refreshLista(i);
								System.out.println("Codigo->" + codigo.getText().toString());
								System.out.println("SKU->"+codigo.getText().toString());
								edcantidad = inst.getCantidadRecoleccion();
								System.out.println("Arrgelo->"+arrayList.get(i).getSkuADO() + "Proveedor"+ arrayList.get(i).getSkuProveedor());
									if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {
										System.out.println("entro");
										inst.setCodigoRecoleccion("");
										inst.setCantidadRecoleccion(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
										codigo.setEnabled(false);
									} else {
										System.out.println("Set en 0.0");
										inst.setCantidadRecoleccion("1.0");
										inst.setCodigoRecoleccion("");
										codigo.setEnabled(false);
									}
								bandera = true;
							}
						}
						if(!bandera){
							inst.noEncontrada();
						}

				}

				if(activity.equalsIgnoreCase("entrada")){
					DarEntradaActivity inst = DarEntradaActivity.instance();
					inst.focus();
					if (inst != null)
						codigo = inst.getCodigoRecoleccion();
					edcantidad = inst.getCantidadRecoleccion();
					if ((codigo.getHint().toString().trim().equals(text.trim()))) {
						System.out.println("Comparando ---->>" + text + "hint:" + codigo.getHint());
						if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {
							System.out.println("entro");
							inst.setCodigoRecoleccion("");
							inst.setCantidadRecoleccion(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
						} else {
							System.out.println("Set en 0.0");
							inst.setCantidadRecoleccion("1.0");
							inst.setCodigoRecoleccion("");
						}
					}
				}


				if(activity.equalsIgnoreCase("ubicacion")){
					UbicacionActivity inst = UbicacionActivity.instance();
					inst.focus();
					if (inst != null)
						codigo = inst.getCodigoRecoleccion();
					edcantidad = inst.getCantidadRecoleccion();
					if (codigo.getHint().toString().trim().equals(text.trim())) {
						System.out.println("Comparando ---->>" + text + "hint:" + codigo.getHint());
						if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {
							System.out.println("entro");
							inst.setCodigoUbicacion("");
							inst.setCantidadUbicacion(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
						} else {
							System.out.println("Set en 0.0");
							inst.setCantidadUbicacion("1.0");
							inst.setCodigoUbicacion("");
						}
					}
				}






				if(activity.equalsIgnoreCase("ubicacion2")){
					System.out.println("HolaMundo desde ubicacion2");
					UbicacionActivity inst = UbicacionActivity.instance();
					inst.focus();
					if (inst != null)
						codigo = inst.getCodigoUbicacion();
					edcantidad = inst.getCantidadRecoleccion();
					String hola2 = text ;
					String[] hola = text.split(",");
					//text = hola[1];
					System.out.println("UnitechText->"+text+"UnitectHola2"+ hola2+"Unitech->"+codigo.getHint().toString()+ "Hola+>"+hola[0]);
					//if (codigo.getHint().toString().trim().equals(text.trim())) {
					if(codigo.getHint().toString().trim().equals(hola[0]) || codigo.getHint().toString().trim().equals(text.trim())){
						System.out.println("Comparando ---->>" + text + "hint:" + codigo.getHint());
						if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {
							System.out.println("CAntidad-Requerida->" + inst.getCantidadRequerida());
                            if(Double.parseDouble(edcantidad.getText().toString())< inst.getCantidadRequerida()) {
                                System.out.println("entro");
                                inst.setCodigoUbicacion("");
                                inst.setCantidadUbicacion(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
                            }else{
                                inst.setMensaje("Atención!","No puedes escanear mas refacciones de las requeridas.");
                                System.out.println("Lanzar Alerta");
                            }
						} else {
							System.out.println("Set en 0.0");
							inst.setCantidadUbicacion("1.0");
							inst.setCodigoUbicacion("");
						}
					}
				}


                //Ubicacion 3
                if(activity.equalsIgnoreCase("ubicacion3")){
                    System.out.println("HolaMundo desde ubicacion2");
                    UbicacionActivity inst = UbicacionActivity.instance();
                    codigo = inst.getCodigoRecoleccion();
					String hola2 = text ;
					String[] hola = text.split(",");
					//text = hola[1];
					System.out.println("Unitech3Text->"+text+"UnitectHola2"+ hola2+"Unitech->"+codigo.getHint().toString()+ "Hola+>"+hola[0]);
					//if (codigo.getHint().toString().trim().equals(text.trim())) {
					if(codigo.getHint().toString().trim().equals(hola[0]) || codigo.getHint().toString().trim().equals(text.trim())){

					System.out.println("Ubicacion3-codigo->"+codigo + "codigoHint->"+codigo.getHint().toString().trim()+"texto.-"+text.trim());
					//if (codigo.getHint().toString().trim().equals(text.trim())) {
                        System.out.println("Comparando ---->>" + text + "hint:" + codigo.getHint());
                       inst.validacionCodigo(codigo.getHint() + "");
                    }
                }

                //Fin Ubicacion 3




                if(activity.equalsIgnoreCase("recepcion")){
                    System.out.println("HolaMundo desde recepcion");
                    DarEntradaActivity inst = DarEntradaActivity.instance();
                    inst.focus();
                    if (inst != null)
                        codigo = inst.getCodigoRecoleccion();
                    edcantidad = inst.getCantidadRecoleccion();

                        if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {

                           if(Double.parseDouble(edcantidad.getText().toString())< inst.getCantidadRequerida()){
							   System.out.println("entro");
							   inst.setCodigoRecoleccion("");
							   inst.setCantidadRecoleccion(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
						   }else{
							   inst.setMensaje("Atención!","No puedes escanear mas refacciones de las requeridas.");
							   System.out.println("Lanzar Alerta");
						   }
                             } else {
                            System.out.println("Set en 0.0");
							inst.setCantidadRecoleccion("1.0");
                            inst.setCodigoRecoleccion("");
                        }
                }

				//Salidas
				if(activity.equalsIgnoreCase("salida")){
					System.out.println("HolaMundo desde salida");
					SalidaActivity inst = SalidaActivity.instance();
					inst.focus();
					if (inst != null)
						codigo = inst.getCodigoRecoleccion();
					edcantidad = inst.getCantidadRecoleccion();
					System.out.println("Comparando ---->>" + text + "hint:" + codigo.getHint());
					if (!edcantidad.getText().toString().isEmpty() && Double.parseDouble(edcantidad.getText().toString()) > 0) {
						inst.setCodigoRecoleccion("");
						inst.setCantidadRecoleccion(Double.toString(Double.parseDouble(edcantidad.getText().toString()) + 1));
						/*if(Double.parseDouble(edcantidad.getText().toString())< inst.getCantidadRequerida()){
							System.out.println("entro");

						}else{
							inst.setMensaje("Atención!","No puedes escanear mas refacciones de las requeridas.");
							System.out.println("Lanzar Alerta");
						}*/
					} else {
						System.out.println("Set en 0.0");
						inst.setCantidadRecoleccion("1.0");
						inst.setCodigoRecoleccion("");
					}
				}
				//finsalidas
			}
	    }
    }
}