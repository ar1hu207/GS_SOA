# HÉLIOS · SOA — Guia de Evidências

Roteiro para **executar e capturar os prints** exigidos no PDF. Cada bloco aponta o que
printar e o resultado esperado (valores reais já validados nos testes).

## 0. Subir os serviços

Pré-requisito: **JDK 21**. Em **dois terminais** na raiz do projeto:

```powershell
# Terminal 1 — Web Service SOAP (porta 8081)
.\mvnw.cmd -pl helios-soap spring-boot:run

# Terminal 2 — API REST (porta 8080)
.\mvnw.cmd -pl helios-rest spring-boot:run
```

Alternativa (após `.\mvnw.cmd clean package`):

```powershell
java -jar helios-soap\target\helios-soap-1.0.0.jar
java -jar helios-rest\target\helios-rest-1.0.0.jar
```

---

## 1. API REST (bloco 25%)

Abra **`docs/requests.http`** (VS Code REST Client / IntelliJ) e rode na ordem. Ou use o **Swagger UI**.

**📸 Prints sugeridos:**
- [ ] **Swagger UI** em `http://localhost:8080/swagger-ui.html` (lista de endpoints).
- [ ] `POST /api/paineis` → **201** (corpo com `id` e `tipo: PAINEL_SOLAR`).
- [ ] `GET /api/paineis` → **200** (lista).
- [ ] `PUT /api/paineis/1` → **200** (nome atualizado).
- [ ] `DELETE` de painel **sem** histórico → **204**.
- [ ] **Console H2** em `http://localhost:8080/h2-console` (JDBC `jdbc:h2:mem:helios`, user `sa`) com `SELECT * FROM PAINEL_SOLAR`.
- [ ] **Erros:** `400` (validação, com `campos`), `409` (código duplicado), `404` (inexistente).

## 2. Web Service SOAP (bloco 25%)

Use **`docs/soap-requests.md`** no **SoapUI** (New SOAP Project → cole o WSDL).

**📸 Prints sugeridos:**
- [ ] **WSDL no navegador**: `http://localhost:8081/ws/diagnostico.wsdl` (mostra as 2 operações).
- [ ] SoapUI: `diagnosticar` (SUJEIRA) → resposta com `causa=SUJEIRA`, `acaoRecomendada=VIBRAR`, `intensidadeVibracao=0.47`.
- [ ] SoapUI: `diagnosticar` (DANO) → `causa=DANO_FISICO`, **sem** `intensidadeVibracao`.
- [ ] SoapUI: `consultarHistorico` → lista dos diagnósticos.
- [ ] SoapUI: **SOAP Fault** com `esperadoEnergia=0`.

## 3. Integração entre serviços (bloco 25%)

O **loop completo**: REST recebe telemetria → consulta NASA POWER → chama SOAP → persiste → alerta/comando.
Rode os passos 5, 6, 7 e 8 do `requests.http` (ou o script abaixo).

### Demo do loop (PowerShell, um clique)

```powershell
$api = "http://localhost:8080/api"
function J($o){ $o | ConvertTo-Json -Compress }

# cria painel (com lat/lon -> NASA POWER ao vivo)
Invoke-RestMethod "$api/paineis" -Method Post -ContentType "application/json" `
  -Body '{"codigo":"PAINEL-A","nome":"Painel A","potenciaNominalW":210.0,"latitude":-23.5,"longitude":-46.6}' | Out-Null

"--- SUJEIRA (deve vibrar) ---"
J (Invoke-RestMethod "$api/paineis/1/leituras" -Method Post -ContentType "application/json" `
  -Body '{"valorEnergia":142.0,"grauSujeira":0.47,"quedaGradual":true,"pontoQuente":false}')

"--- DANO (NAO deve vibrar) ---"
J (Invoke-RestMethod "$api/paineis/1/leituras" -Method Post -ContentType "application/json" `
  -Body '{"valorEnergia":120.0,"grauSujeira":0.02,"quedaGradual":false,"pontoQuente":true}')

"--- ALERTAS ABERTOS ---"
J (Invoke-RestMethod "$api/alertas?apenasAbertos=true")
```

**📸 Prints sugeridos:**
- [ ] Resposta do loop **SUJEIRA**: `ambiente.fonte = "nasa-power"` (prova o consumo externo) + `comando VIBRAR`.
- [ ] Resposta do loop **DANO**: `comando = null` (prova que o sistema "pensa": não vibra painel trincado).
- [ ] `GET /api/paineis/1/diagnosticos` com os dois diagnósticos.
- [ ] **Resiliência:** derrube o `helios-soap` e refaça o `POST /leituras` → **503** (REST não trava).

**Resultado real esperado (SUJEIRA):**

```json
{ "ambiente": { "solBaixo": false, "frio": false, "fonte": "nasa-power" },
  "diagnostico": { "causa": "SUJEIRA", "acaoRecomendada": "VIBRAR", "intensidadeVibracao": 0.47 },
  "comando": { "atuadorId": "VIB-PAINEL-A", "acao": "VIBRAR", "intensidade": 0.47, "duracaoSeg": 6 },
  "alerta": { "tipo": "SUJEIRA_DETECTADA", "severidade": "ALTA" } }
```

## 4. Checklist final (bloco Documentação 25%)

- [ ] Prints dos blocos 1, 2 e 3 colados no PDF.
- [ ] `DOCUMENTACAO.md` exportado para PDF com **nomes + RM** preenchidos.
- [ ] Diagrama de arquitetura SOA e diagrama de sequência presentes (já em `DOCUMENTACAO.md`).
- [ ] Repositório público no GitHub com README.
