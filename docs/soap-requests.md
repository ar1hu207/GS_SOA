# HÉLIOS · SOAP — envelopes prontos (SoapUI / curl)

- **WSDL:** `http://localhost:8081/ws/diagnostico.wsdl`
- **Endpoint:** `http://localhost:8081/ws`
- **No SoapUI:** New SOAP Project → cole a URL do WSDL → ele gera as requisições das duas operações.

---

## 1. `diagnosticar` — SUJEIRA (vibra)

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:dia="http://helios.fiap.com.br/soap/diagnostico">
  <soapenv:Body>
    <dia:diagnosticarRequest>
      <dia:ativoId>PAINEL-A</dia:ativoId>
      <dia:valorEnergia>142.0</dia:valorEnergia>
      <dia:esperadoEnergia>210.0</dia:esperadoEnergia>
      <dia:grauSujeira>0.47</dia:grauSujeira>
      <dia:quedaGradual>true</dia:quedaGradual>
      <dia:pontoQuente>false</dia:pontoQuente>
      <dia:solBaixo>false</dia:solBaixo>
      <dia:frio>false</dia:frio>
    </dia:diagnosticarRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

Esperado: `causa = SUJEIRA`, `acaoRecomendada = VIBRAR`, `intensidadeVibracao = 0.47`.

## 2. `diagnosticar` — DANO_FISICO (não vibra)

Troque o corpo por: `valorEnergia=120`, `grauSujeira=0.02`, `quedaGradual=false`,
`pontoQuente=true`, `solBaixo=false`, `frio=false`.
Esperado: `causa = DANO_FISICO`, `acaoRecomendada = ALERTA_MANUTENCAO`, **sem** `intensidadeVibracao`.

## 3. `diagnosticar` — SOAP Fault (entrada inválida)

Use `esperadoEnergia=0` (ou `grauSujeira=1.5`). Esperado: **SOAP Fault** (faultcode Client)
com a mensagem de validação.

## 4. `consultarHistorico` — consulta

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:dia="http://helios.fiap.com.br/soap/diagnostico">
  <soapenv:Body>
    <dia:consultarHistoricoRequest>
      <dia:ativoId>PAINEL-A</dia:ativoId>
    </dia:consultarHistoricoRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

Esperado: lista dos diagnósticos já feitos para `PAINEL-A` (após chamar `diagnosticar`).

---

## curl (alternativa ao SoapUI)

```bash
curl -s -X POST http://localhost:8081/ws \
  -H "Content-Type: text/xml; charset=utf-8" -H "SOAPAction:" \
  --data-binary @diagnosticar-sujeira.xml
```

> Salve o envelope da seção 1 em `diagnosticar-sujeira.xml`. No Windows/PowerShell, use
> `Invoke-WebRequest` com `-Body` (ver `docs/EVIDENCIAS.md`).
