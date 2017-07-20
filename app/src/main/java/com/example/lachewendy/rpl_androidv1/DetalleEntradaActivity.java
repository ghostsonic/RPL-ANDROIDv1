package com.example.lachewendy.rpl_androidv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetalleEntradaActivity extends AppCompatActivity {

    TextView txtNumOrden;
    TextView txtFechaPedido;
    TextView txtUnidadesTotales;
    TextView txtAlmacen;
    TextView txtProcendencia;
    TextView txtBackToolbar;
    TextView txtTituloToolbar;

    Button btnDarEntrada;
    Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_entrada);

        txtNumOrden = (TextView) findViewById(R.id.txtNumOrdenEntrada);
        txtFechaPedido = (TextView) findViewById(R.id.txtFechaPedidoEntrada);
        txtUnidadesTotales = (TextView) findViewById(R.id.txtUnidadesTotalesEntrada);
        txtAlmacen = (TextView) findViewById(R.id.txtAlmacenEntrada);
        txtProcendencia = (TextView) findViewById(R.id.txtProcedenciaEntrada);
        txtBackToolbar = (TextView) findViewById(R.id.txtBackToolbar);
        txtTituloToolbar = (TextView) findViewById(R.id.txtTituloToolbar);

        btnDarEntrada = (Button) findViewById(R.id.btnDarEntrada);
        btnCancelar = (Button) findViewById(R.id.btnCancelarEntrada);

        eventos();






    }


    public void eventos(){

        btnDarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
