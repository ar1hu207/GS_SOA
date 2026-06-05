# Evidências de funcionamento — HÉLIOS · SOA

Capturadas com os dois serviços no ar (`helios-soap :8081` + `helios-rest :8080`).
Use estes arquivos no PDF de entrega.

## Screenshots

| Arquivo | O que mostra | Bloco da nota |
|---|---|---|
| `01-swagger-ui.png` | Swagger UI com todos os endpoints REST (3 grupos) | API REST |
| `02-wsdl.png` | WSDL publicado do Web Service SOAP (no navegador) | SOAP |
| `03-rest-paineis.png` | `GET /api/paineis` (JSON do CRUD) | API REST |
| `04-rest-diagnosticos.png` | `GET /api/paineis/1/diagnosticos` (SUJEIRA + DANO persistidos) | Integração / Persistência |
| `05-rest-alertas.png` | `GET /api/alertas` (alertas gerados pelo loop) | Integração |
| `06-h2-console.png` | Console do H2 (login da base) | Persistência |

## Transcript textual

- **`saida-testes.md`** — saídas **reais** de todas as chamadas: CRUD REST, validação/erros
  (`400/404/409/415`), o loop completo (SUJEIRA e DANO), a **matriz SOAP 6/6**, o **SOAP Fault**,
  `consultarHistorico` e a **resiliência `503`** (SOAP fora).

## Observações para a banca

- **`ambiente.fonte`**: aparece como `nasa-power` quando a chamada à NASA POWER responde, e
  `simulado` no fallback (resiliência) — ambos válidos. A integração externa é real (ver `AmbienteService`).
- **SOAP via SoapUI**: estes prints usam o WSDL no navegador + o transcript. Para prints no
  **SoapUI**, use os envelopes de [`../soap-requests.md`](../soap-requests.md) (a banca pede SoapUI).
- **Como reproduzir tudo**: ver [`../EVIDENCIAS.md`](../EVIDENCIAS.md).

> Dica: o caso **DANO** (sem `comando` de vibração) e a linha "DANO mesmo com sol baixo+frio"
> na matriz provam que o sistema **diagnostica** (não vibra painel trincado) — bom destaque no vídeo/pitch.
