package com.example.mywebview

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.SearchView
import android.webkit.WebViewClient

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val BaseUrl="https://www.google.com"
    private val SEARCHPATH ="/search?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Refresh aqui refresca la pagina web

        swipeRefresh.setOnRefreshListener {
            webView.reload()

        }

        // search  se implemeta listener para hacer el quuery y se implementa Onquerysearch view
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            // se implmentan dos operaciones para interactuar con la barra de busqueda

            override fun onQueryTextChange(p0: String?): Boolean {
                return false // aqui nos permite si queremos como actuar cuando se realiza una busqueda
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0.let {
                    if (URLUtil.isValidUrl(it)) {
                        // es una URL
                        webView.loadUrl(it)

                    } else {
                        // no es una URL
                        webView.loadUrl("$BaseUrl$SEARCHPATH$it")
                    }
                }
                return false // se invoca cuando el usuario escribe algo y da clic en buscar
            }

        })



        //webview - se crea un cliente  webchrome y cliente webview
        webView.webChromeClient = object : WebChromeClient() {
        }


        webView.webViewClient = object : WebViewClient() {

            // cede el control del manejo de nuevas de url
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            // vemos cuando una nueva url empieza a cagar
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                searchView.setQuery(url,false) // esto permite mostrar url en la barra
                swipeRefresh.isRefreshing = true


            }
                /* cuando finalice el refresh se vuelve falso el swiperefresh */
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false

            }

        }

        // se activa javascript
        val settings = webView.settings
        settings.javaScriptEnabled = true

        // cargar la url
        webView.loadUrl(BaseUrl)

    }

        // se programa ir atras que regrese a la pagina principal
        override fun onBackPressed() {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                super.onBackPressed()
            }
        }
    }


