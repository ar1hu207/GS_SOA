# HÉLIOS · SOA — Plano de Execução (Sprints & CPs)

> Plano para desenvolver o módulo SOA do HÉLIOS. Contexto, arquitetura e contrato
> de dados estão no [DOCUMENTACAO.md](DOCUMENTACAO.md). Aqui está **a ordem de construção**.
>
> **Entidade principal do CRUD: `PainelSolar` (decidido).**
> Pacote base: `br.com.fiap.helios`.

---

## Como ler este plano

- **Sprint** = fase de construção com um objetivo único e entregável próprio.
- **CP** (Checkpoint) = ponto de validação ao fim da sprint. Só avança quando o
  "Definition of Done" estiver verde. Cada CP aponta qual % da nota ele alimenta.
- Construímos **na ordem do fluxo de dados**: primeiro o cérebro (SOAP), depois a
  porta de entrada (REST), depois a cola (integração), por fim a prova (docs).

### Mapa Sprint → Nota

| Sprint | Entrega | Bloco da nota |
|---|---|---|
| S0 | Fundação / setup Maven | (habilita tudo) |
| S1 | Web Service SOAP | **SOAP 25%** |
| S2 | API REST + H2 | **REST 25%** |
| S3 | Integração REST↔SOAP + API externa | **Integração 25%** |
| S4 | Documentação + diagrama + PDF | **Documentação 25%** |
| S5 | Diferenciais (opcional) | pontos extras |

---

## Sprint 0 — Fundação & Setup ✅ CONCLUÍDA

**Objetivo:** repositório e esqueleto Maven multi-módulo de pé, tudo compilando e subindo.

**Tarefas:**
- [ ] `git init` + repositório GitHub **público** *(pendente — fazer quando o grupo definir o repo)*
- [x] `.gitignore` (Java/Maven/IDE)
- [x] POM **pai** (`helios-soa`) com `packaging=pom` e os dois módulos
- [x] Módulo `helios-soap` (Spring Boot, porta 8081) — sobe vazio
- [x] Módulo `helios-rest` (Spring Boot, porta 8080) — sobe vazio
- [x] H2 configurado no `helios-rest` (`/h2-console` acessível)
- [x] Swagger/OpenAPI plugado no `helios-rest` (`/swagger-ui.html`)
- [x] README inicial (faltam nomes + RM dos integrantes)
- [x] Maven Wrapper (`mvnw`/`mvnw.cmd`) gerado — projeto autossuficiente

**CP0 — Definition of Done:** ✅
- [x] `mvn clean install` na raiz compila os dois módulos sem erro.
- [x] Os dois apps sobem (`8080` e `8081`); H2 console e Swagger abrem (200/200/200; SOAP 8081 responde 404, server up).
- [ ] Repositório público com README e integrantes *(pendente — repo + RM)*.

> Ambiente: JDK 21 (Microsoft OpenJDK) + Maven 3.9.9 instalado em
> `%LOCALAPPDATA%\Programs\maven`. Spring Boot 3.3.5.

---

## Sprint 1 — Web Service SOAP (Diagnóstico) · 25% ✅ CONCLUÍDA

**Objetivo:** o cérebro do HÉLIOS como Web Service SOAP **contract-first**, com WSDL
publicado e testável no SoapUI. Aqui moram herança e polimorfismo (POO).

**Tarefas:**
- [x] `diagnostico.xsd` (contract-first): tipos de request/response p/ as 2 operações
- [x] Geração do **WSDL** a partir do XSD (Spring-WS `DefaultWsdl11Definition`)
- [x] `WebServiceConfig` (MessageDispatcherServlet + endpoint do WSDL + Jaxb2Marshaller)
- [x] Domínio `Causa` **(abstrata)** → `Sujeira`, `Sombra`, `DanoFisico`, `FalhaEletrica`, `Ambiental`, `SemFalha`
- [x] `MotorDiagnostico` — fusão de sinais conforme a **matriz de diagnóstico** (DOCUMENTACAO.md §6.4)
- [x] `DiagnosticoEndpoint` com:
  - [x] **Processamento:** `diagnosticar(energia, grauSujeira, ambiente)` → causa + confiança + evidências
  - [x] **Consulta:** `consultarHistorico(ativoId)` → lista de diagnósticos
- [x] Histórico em memória (`HistoricoDiagnostico`) — persistência durável fica no REST
- [x] Tratamento de exceção: `DiagnosticoInvalidoException` mapeada para SOAP Fault (Client)

**CP1 — Definition of Done:** ✅
- [x] WSDL acessível em `http://localhost:8081/ws/diagnostico.wsdl` (operações `diagnosticar` + `consultarHistorico`).
- [x] `diagnosticar` (sujeira) → `SUJEIRA`/`VIBRAR`/intensidade 0.47; (dano) → `DANO_FISICO`/`ALERTA_MANUTENCAO`/**sem vibração**.
- [x] Polimorfismo demonstrado (cada `Causa` decide `severidade()`/`acaoRecomendada()`/`intensidadeVibracao()`).
- [x] `consultarHistorico` retorna os diagnósticos acumulados do painel.
- [ ] **Prints** do SoapUI em `/docs/evidencias/soap/` *(coletar na fase de documentação — validado via HTTP por enquanto)*.

---

## Sprint 2 — API REST + Persistência (PainelSolar) · 25% ✅ CONCLUÍDA

**Objetivo:** CRUD completo de `PainelSolar` em Spring Boot + H2, com JSON, validação e
tratamento de erros.

**Tarefas:**
- [x] Domínio JPA: `Ativo` **(abstrata, @MappedSuperclass)** → `PainelSolar` (raiz)
- [x] Repositório Spring Data JPA (`PainelSolarRepository` + findByCodigo/existsByCodigo)
- [x] DTOs (`PainelSolarRequest` com validação, `PainelSolarResponse`, `ErroResposta`)
- [x] `PainelSolarController` — **CRUD completo**:
  - [x] `GET /api/paineis` (listar) e `GET /api/paineis/{id}` (detalhe)
  - [x] `POST /api/paineis` (cadastrar → 201 + Location)
  - [x] `PUT /api/paineis/{id}` (atualizar)
  - [x] `DELETE /api/paineis/{id}` (remover → 204)
- [x] **Validação** (Bean Validation `@Valid`, `@NotBlank`, `@Positive`)
- [x] **Tratamento de erros** global (`@RestControllerAdvice`: 400 validação, 404, 409 regra, 500)
- [x] Documentação Swagger anotada (`@Tag`, `@Operation`, `OpenApiConfig`)

> Nota: `Leitura`, `Diagnostico` e `Alerta` foram adiados para a Sprint 3 (integração),
> onde são realmente usados — mantém o CRUD da S2 enxuto.

**CP2 — Definition of Done:** ✅
- [x] CRUD completo funcionando: POST 201 · GET 200 · PUT 200 · DELETE 204.
- [x] Erros corretos: 409 (código duplicado), 400 (validação com campos), 404 (não encontrado).
- [x] Dados persistem no H2; respostas JSON padronizadas.
- [ ] **Prints** em `/docs/evidencias/rest/` *(coletar na documentação — validado via HTTP)*.

---

## Sprint 3 — Integração (o loop HÉLIOS) · 25% ✅ CONCLUÍDA

**Objetivo:** fechar o loop. O REST recebe telemetria, consulta a API externa, chama o
SOAP de diagnóstico, persiste e gera alerta/comando. **Aqui se prova a SOA.**

**Tarefas:**
- [x] Cliente **SOAP** no helios-rest (`WebServiceTemplate` + contrato JAXB) consumindo o `helios-soap`
- [x] `AmbienteService` (consumo de serviço REST externo OpenWeatherMap) com **fallback simulado** e tratamento de falha
- [x] Entidades de apoio + repositórios: `Leitura`, `Diagnostico`, `Alerta`
- [x] Endpoint de telemetria: `POST /api/paineis/{id}/leituras`
  - [x] persiste a `Leitura`
  - [x] busca `ambiente` na API externa (ou simulado)
  - [x] chama `diagnosticar` (SOAP) → recebe a `Causa`
  - [x] persiste o `Diagnostico`; se `SUJEIRA` → gera `ComandoVibracao`; severidade ALTA → `Alerta`
- [x] Endpoints de leitura: `GET /api/paineis/{id}/diagnosticos`, `GET /api/alertas`, `PUT /api/alertas/{id}/reconhecer`
- [x] Exceção `ServicoIndisponivelException` (503) quando o SOAP falha

**CP3 — Definition of Done:** ✅
- [x] Fluxo end-to-end: `POST` de leitura dispara externo → SOAP → persistência → alerta/comando.
- [x] **SUJEIRA** gera comando de vibração (int. 0.47, 6s); **DANO** gera alerta e **não** vibra (comando=null).
- [x] Histórico de diagnósticos e alertas persistido no H2 e consultável.
- [ ] **Prints** da integração em `/docs/evidencias/integracao/` *(coletar na documentação — validado via HTTP)*.

> Pendência (não bloqueante): a chamada externa **real** usa OpenWeatherMap quando há
> `OPENWEATHER_API_KEY`. Sem chave, o loop roda com ambiente simulado. Definir provedor + chave.

---

## Sprint 4 — Documentação & Entrega · 25%

**Objetivo:** produzir o PDF e a documentação que valem o último quarto da nota.

**Tarefas:**
- [ ] **Diagrama de Arquitetura SOA** (REST · SOAP · externa · H2 · fluxo)
- [ ] Doc **REST**: endpoints, métodos HTTP, exemplos de requisição/resposta
- [ ] Doc **SOAP**: operações, estruturas XML, link do WSDL, exemplos req/resp
- [ ] Explicação da **integração** entre serviços
- [ ] README final completo (descrição, execução, tecnologias, integrantes+RM)
- [ ] **PDF** com todos os itens exigidos (ver DOCUMENTACAO.md)
- [ ] Conferir **ODS** + conexão com **Space Connect** explícitas
- [ ] Revisar checklist dos 4 pilares de POO e dos princípios de SOA

**CP4 — Definition of Done:**
- PDF completo com diagrama, evidências e prints.
- Código-fonte final no repositório público.
- Checklist do enunciado 100% verde.

---

## Sprint 5 — Diferenciais (opcional, se houver tempo)

- [ ] **Docker**: `docker-compose` subindo os dois serviços
- [ ] **Testes automatizados** (JUnit + MockMvc no REST; teste do endpoint SOAP)
- [ ] **Mensageria** (RabbitMQ para os eventos de alerta/comando)
- [ ] **Gateway** de APIs
- [ ] Integração com **múltiplos** serviços externos (NASA + clima)

---

## Ordem recomendada para eu começar a codar

1. **Sprint 0** inteira (esqueleto Maven dos dois módulos). ← próximo passo
2. **Sprint 1** (SOAP) — coração do diagnóstico, contract-first.
3. **Sprint 2** (REST + H2).
4. **Sprint 3** (integração).
5. **Sprint 4** (docs/PDF).

> Pendência para destravar a Sprint 3: escolher a **API externa** (NASA ou OpenWeatherMap).
> Pode ser decidido durante a Sprint 1/2, sem bloquear o início.
