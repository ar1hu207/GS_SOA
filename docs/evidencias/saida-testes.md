# HÉLIOS · SOA — Transcript de Evidências (saídas reais)

> Gerado executando os dois serviços (helios-soap :8081 + helios-rest :8080). Respostas e timestamps reais.

> Nota: o campo `ambiente.fonte` aparece como `nasa-power` quando a chamada à NASA POWER responde, e `simulado` no fallback (resiliência). Ambos são comportamentos válidos.

## 1. API REST — CRUD

**POST /api/paineis (cadastrar)** -> HTTP 201
```json
{"id":1,"codigo":"PAINEL-A","nome":"Painel A - Base Sul","tipo":"PAINEL_SOLAR","potenciaNominalW":210.0,"latitude":-23.5,"longitude":-46.6,"ativo":true}
```

**GET /api/paineis (listar)** -> HTTP 200
```json
[{"id":1,"codigo":"PAINEL-A","nome":"Painel A - Base Sul","tipo":"PAINEL_SOLAR","potenciaNominalW":210.0,"latitude":-23.5,"longitude":-46.6,"ativo":true}]
```

**PUT /api/paineis/1 (atualizar)** -> HTTP 200
```json
{"id":1,"codigo":"PAINEL-A","nome":"Painel A - atualizado","tipo":"PAINEL_SOLAR","potenciaNominalW":210.0,"latitude":-23.5,"longitude":-46.6,"ativo":false}
```

## 2. API REST — validação e tratamento de erros

**POST inválido (400 com campos)** -> HTTP 400
```json
{"timestamp":"2026-06-04T20:43:16.2169417","status":400,"erro":"Bad Request","mensagem":"Falha de validação nos campos enviados.","caminho":"/api/paineis","campos":{"codigo":"codigo é obrigatório","nome":"nome é obrigatório","potenciaNominalW":"potenciaNominalW deve ser maior que zero"}}
```

**POST código duplicado (409)** -> HTTP 409
```json
{"timestamp":"2026-06-04T20:43:16.2510895","status":409,"erro":"Conflict","mensagem":"Já existe um painel com o código PAINEL-A","caminho":"/api/paineis","campos":null}
```

**POST JSON malformado (400, msg neutra)** -> HTTP 400
```json
{"timestamp":"2026-06-04T20:43:16.2635215","status":400,"erro":"Bad Request","mensagem":"Corpo da requisição ausente ou em formato inválido.","caminho":"/api/paineis","campos":null}
```

**GET id não numérico (400)** -> HTTP 400
```json
{"timestamp":"2026-06-04T20:43:20.3671567","status":400,"erro":"Bad Request","mensagem":"Parâmetro 'id' com valor inválido.","caminho":"/api/paineis/abc","campos":null}
```

**GET inexistente (404)** -> HTTP 404
```json
{"timestamp":"2026-06-04T20:43:24.186648","status":404,"erro":"Not Found","mensagem":"Painel não encontrado para o id 99999","caminho":"/api/paineis/99999","campos":null}
```

## 3. Loop de integração (REST -> NASA POWER -> SOAP -> persistência)

**POST /paineis/1/leituras — SUJEIRA (vibra)** -> HTTP 200
```json
{"painelId":1,"ativoId":"PAINEL-A","leituraId":1,"ambiente":{"solBaixo":false,"frio":false,"fonte":"nasa-power"},"diagnostico":{"causa":"SUJEIRA","confianca":0.788,"severidade":"ALTA","acaoRecomendada":"VIBRAR","intensidadeVibracao":0.47,"evidencias":["queda_0.32","cobertura_0.47","queda_gradual","ambiente_normal"],"timestamp":"2026-06-04T20:43:25.341056"},"comando":{"atuadorId":"VIB-PAINEL-A","acao":"VIBRAR","intensidade":0.47,"duracaoSeg":6},"alerta":{"id":1,"severidade":"ALTA","tipo":"SUJEIRA_DETECTADA","mensagem":"Perda de 32% — causa SUJEIRA. Ação recomendada: VIBRAR.","resolvido":false}}
```

**POST /paineis/1/leituras — DANO (não vibra)** -> HTTP 200
```json
{"painelId":1,"ativoId":"PAINEL-A","leituraId":2,"ambiente":{"solBaixo":false,"frio":false,"fonte":"nasa-power"},"diagnostico":{"causa":"DANO_FISICO","confianca":0.85,"severidade":"ALTA","acaoRecomendada":"ALERTA_MANUTENCAO","intensidadeVibracao":null,"evidencias":["queda_0.43","ponto_quente"],"timestamp":"2026-06-04T20:43:26.198379100"},"comando":null,"alerta":{"id":2,"severidade":"ALTA","tipo":"DANO_FISICO_DETECTADA","mensagem":"Perda de 43% — causa DANO_FISICO. Ação recomendada: ALERTA_MANUTENCAO.","resolvido":false}}
```

**GET /paineis/1/diagnosticos** -> HTTP 200
```json
[{"id":2,"ativoId":"PAINEL-A","causa":"DANO_FISICO","confianca":0.85,"severidade":"ALTA","acaoRecomendada":"ALERTA_MANUTENCAO","intensidadeVibracao":null,"evidencias":["queda_0.43","ponto_quente"],"criadoEm":"2026-06-04T20:43:25.457208"},{"id":1,"ativoId":"PAINEL-A","causa":"SUJEIRA","confianca":0.788,"severidade":"ALTA","acaoRecomendada":"VIBRAR","intensidadeVibracao":0.47,"evidencias":["queda_0.32","cobertura_0.47","queda_gradual","ambiente_normal"],"criadoEm":"2026-06-04T20:43:24.201515"}]
```

**GET /alertas?apenasAbertos=true** -> HTTP 200
```json
[{"id":2,"ativoId":"PAINEL-A","severidade":"ALTA","tipo":"DANO_FISICO_DETECTADA","mensagem":"Perda de 43% — causa DANO_FISICO. Ação recomendada: ALERTA_MANUTENCAO.","resolvido":false,"criadoEm":"2026-06-04T20:43:25.457208"},{"id":1,"ativoId":"PAINEL-A","severidade":"ALTA","tipo":"SUJEIRA_DETECTADA","mensagem":"Perda de 32% — causa SUJEIRA. Ação recomendada: VIBRAR.","resolvido":false,"criadoEm":"2026-06-04T20:43:24.201515"}]
```

## 4. DELETE com regra de integridade

**DELETE painel COM histórico (409)** -> HTTP 409
```json
{"timestamp":"2026-06-04T20:43:33.703964","status":409,"erro":"Conflict","mensagem":"Painel PAINEL-A possui histórico de telemetria (6 registros) e não pode ser removido. Desative-o (PUT com ativo=false) ou remova o histórico antes.","caminho":"/api/paineis/1","campos":null}
```

**DELETE painel SEM histórico (204)** -> HTTP 204
```json

```

## 5. Web Service SOAP — matriz de diagnóstico

| Cenário | Causa retornada |
|---|---|
| SemFalha (205/210) | SEM_FALHA |
| Sujeira (grau .47, gradual) | SUJEIRA |
| DanoFisico (ponto quente) | DANO_FISICO |
| Ambiental (sol baixo) | AMBIENTAL |
| FalhaEletrica (limpo, queda) | FALHA_ELETRICA |
| Sombra (grau .10, súbita) | SOMBRA |
| DANO mesmo com sol baixo+frio (não mascara) | DANO_FISICO |

### Resposta SOAP completa — diagnosticar (SUJEIRA)
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><ns2:diagnosticarResponse xmlns:ns2="http://helios.fiap.com.br/soap/diagnostico"><ns2:diagnostico><ns2:ativoId>PAINEL-A</ns2:ativoId><ns2:causa>SUJEIRA</ns2:causa><ns2:confianca>0.788</ns2:confianca><ns2:severidade>ALTA</ns2:severidade><ns2:acaoRecomendada>VIBRAR</ns2:acaoRecomendada><ns2:intensidadeVibracao>0.47</ns2:intensidadeVibracao><ns2:evidencia>queda_0.32</ns2:evidencia><ns2:evidencia>cobertura_0.47</ns2:evidencia><ns2:evidencia>queda_gradual</ns2:evidencia><ns2:evidencia>ambiente_normal</ns2:evidencia><ns2:timestamp>2026-06-04T20:43:33.979664900</ns2:timestamp></ns2:diagnostico></ns2:diagnosticarResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>
```

### SOAP Fault — entrada inválida (esperadoEnergia=0) -> HTTP 500
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><SOAP-ENV:Fault><faultcode>SOAP-ENV:Client</faultcode><faultstring xml:lang="en">esperadoEnergia deve ser maior que zero.</faultstring></SOAP-ENV:Fault></SOAP-ENV:Body></SOAP-ENV:Envelope>
```

### consultarHistorico (PAINEL-A)
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><ns2:consultarHistoricoResponse xmlns:ns2="http://helios.fiap.com.br/soap/diagnostico"><ns2:diagnostico><ns2:ativoId>PAINEL-A</ns2:ativoId><ns2:causa>SUJEIRA</ns2:causa><ns2:confianca>0.788</ns2:confianca><ns2:severidade>ALTA</ns2:severidade><ns2:acaoRecomendada>VIBRAR</ns2:acaoRecomendada><ns2:intensidadeVibracao>0.47</ns2:intensidadeVibracao><ns2:evidencia>queda_0.32</ns2:evidencia><ns2:evidencia>cobertura_0.47</ns2:evidencia><ns2:evidencia>queda_gradual</ns2:evidencia><ns2:evidencia>ambiente_normal</ns2:evidencia><ns2:timestamp>2026-06-04T20:43:25.341056</ns2:timestamp></ns2:diagnostico><ns2:diagnostico><ns2:ativoId>PAINEL-A</ns2:ativoId><ns2:causa>DANO_FISICO</ns2:causa><ns2:confianca>0.85</ns2:confianca><ns2:severidade>ALTA</ns2:severidade><ns2:acaoRecomendada>ALERTA_MANUTENCAO</ns2:acaoRecomendada><ns2:evidencia>queda_0.43</ns2:evidencia><ns2:evidencia>ponto_quente</ns2:evidencia><ns2:timestamp>2026-06-04T20:43:26.198379100</ns2:timestamp></ns2:diagnostico><ns2:diagnostico><ns2:ativoId>PAINEL-A</ns2:ativoId><ns2:causa>SUJEIRA</ns2:causa><ns2:confianca>0.788</ns2:confianca><ns2:severidade>ALTA</ns2:severidade><ns2:acaoRecomendada>VIBRAR</ns2:acaoRecomendada><ns2:intensidadeVibracao>0.47</ns2:intensidadeVibracao><ns2:evidencia>queda_0.32</ns2:evidencia><ns2:evidencia>cobertura_0.47</ns2:evidencia><ns2:evidencia>queda_gradual</ns2:evidencia><ns2:evidencia>ambiente_normal</ns2:evidencia><ns2:timestamp>2026-06-04T20:43:33.979664900</ns2:timestamp></ns2:diagnostico></ns2:consultarHistoricoResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>
```

## 6. Resiliência — SOAP indisponível (REST devolve 503)

**POST /paineis/1/leituras com SOAP fora (503)** -> HTTP 503
```json
{"timestamp":"2026-06-04T20:43:36.5411454","status":503,"erro":"Service Unavailable","mensagem":"Serviço SOAP de diagnóstico indisponível.","caminho":"/api/paineis/1/leituras","campos":null}
```
