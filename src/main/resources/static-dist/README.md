# Diretório de Distribuição do Frontend

Este diretório (`static-dist`) é destinado a armazenar os arquivos compilados do frontend Next.js da aplicação Shift-Flare.

## Como funciona

1. O frontend Next.js é desenvolvido separadamente na pasta raiz do projeto
2. Durante o build, os arquivos estáticos do Next.js são exportados
3. Os arquivos exportados são copiados para este diretório
4. O Spring Boot serve estes arquivos como recursos estáticos

## Configuração no Spring Boot

No arquivo `application.properties`, a seguinte configuração indica ao Spring Boot que deve servir os arquivos deste diretório:

```
spring.web.resources.static-locations=classpath:/static-dist/
```

## Processo de Build Integrado

O processo de build completo tipicamente segue estes passos:

1. Build do frontend Next.js (`npm run build`)
2. Exportação dos arquivos estáticos (`next export`) para a pasta `out/`
3. Cópia dos arquivos da pasta `out/` para esta pasta `static-dist/`
4. Build do backend Spring Boot com Maven

Para facilitar este processo, você pode utilizar o script de build integrado na raiz do projeto.

## Integração Frontend-Backend

Esta abordagem permite manter o frontend e o backend separados durante o desenvolvimento, mas integrados na entrega final, disponibilizando toda a aplicação através de um único servidor Spring Boot. 