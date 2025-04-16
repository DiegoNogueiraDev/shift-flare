RECURSOS LOCAIS - INSTRUÇÕES DE DOWNLOAD

Você precisará baixar os seguintes arquivos manualmente para uso local:

1. Bootstrap CSS:
   - Baixe o arquivo em: https://getbootstrap.com/docs/5.3/getting-started/download/
   - Salve como: src/main/resources/static/css/bootstrap.min.css

2. Bootstrap Bundle JS:
   - Também incluído no download acima
   - Salve como: src/main/resources/static/js/bootstrap.bundle.min.js

3. Bootstrap Icons:
   - Baixe em: https://icons.getbootstrap.com/
   - Salve o CSS como: src/main/resources/static/css/bootstrap-icons.css
   - Salve as fontes em: src/main/resources/static/fonts/bootstrap-icons/

4. Highlight.js:
   - Baixe em: https://highlightjs.org/download/
   - Salve o JS como: src/main/resources/static/js/highlight.min.js
   - Salve o CSS como: src/main/resources/static/css/highlight/atom-one-dark.min.css

5. Fontes Poppins:
   - Baixe em: https://fonts.google.com/specimen/Poppins
   - Converta para woff/woff2 se necessário
   - Salve em: src/main/resources/static/fonts/poppins/

Ou, alternativamente, você pode criar um script para automatizar o download desses recursos.

Estrutura de Diretórios:
```
src/main/resources/static/
├── css/
│   ├── bootstrap.min.css
│   ├── bootstrap-icons.css
│   ├── poppins.css
│   ├── styles.css
│   └── highlight/
│       └── atom-one-dark.min.css
├── js/
│   ├── bootstrap.bundle.min.js
│   ├── component-loader.js
│   ├── highlight.min.js
│   └── utils.js
└── fonts/
    ├── bootstrap-icons/
    │   └── ... (arquivos de fonte)
    └── poppins/
        ├── poppins-light.woff
        ├── poppins-light.woff2
        ├── poppins-regular.woff
        ├── poppins-regular.woff2
        └── ... (outros pesos de fonte)
``` 