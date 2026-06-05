# HÉLIOS — Módulo SOA

Global Solution 2026.1 · FIAP · Tema **Space Connect**

Arquitetura orientada a serviços do **HÉLIOS**, sistema autônomo que mantém a energia
de uma base lunar limpando painéis solares por vibração. Este repositório contém a
camada de **serviços distribuídos** (SOA): uma **API REST** e um **Web Service SOAP**
que se integram para fechar o loop *perceber → diagnosticar → agir → verificar*.

## Integrantes

- Arthur Abonizio — RM 555506
- Gabriel Padula — RM 554907
- Rodrigo Nakata — RM 556417

## Arquitetura

| Módulo | Porta | Papel |
|---|---|---|
| `helios-rest` | 8080 | API REST (CRUD de `PainelSolar`), persistência H2, orquestração do loop, cliente SOAP e da API externa |
| `helios-soap` | 8081 | Web Service SOAP de diagnóstico (fusão de sinais → causa), WSDL publicado |

Detalhes do plano de execução em [PLANO_EXECUCAO.md](PLANO_EXECUCAO.md) (sprints e checkpoints).

## Tecnologias

Java 21 · Spring Boot 3.3 · Spring Web · Spring Web Services (SOAP contract-first) ·
Spring Data JPA · H2 · springdoc-openapi (Swagger) · Maven (multi-módulo) ·
NASA POWER API (serviço externo de ambiente).

## Documentação & evidências

- **[DOCUMENTACAO.md](DOCUMENTACAO.md)** — documento de entrega (descrição, problema, arquitetura, diagramas, REST, SOAP, integração, ODS, conclusão). Exporte para PDF.
- **[docs/EVIDENCIAS.md](docs/EVIDENCIAS.md)** — roteiro de testes/prints (REST, SOAP e loop completo).
- **[docs/requests.http](docs/requests.http)** — requisições REST prontas · **[docs/soap-requests.md](docs/soap-requests.md)** — envelopes SOAP.
- **[PLANO_EXECUCAO.md](PLANO_EXECUCAO.md)** — plano de execução (sprints e checkpoints).

## Como rodar

Pré-requisitos: **JDK 21**. Maven não é obrigatório (use o wrapper `./mvnw`).

```bash
# Buildar tudo
./mvnw clean install        # Windows: .\mvnw.cmd clean install

# Subir o Web Service SOAP (porta 8081)
./mvnw -pl helios-soap spring-boot:run

# Subir a API REST (porta 8080)
./mvnw -pl helios-rest spring-boot:run
```

Endpoints úteis (REST):
- Swagger UI: http://localhost:8080/swagger-ui.html
- Console H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:helios`, user `sa`)
