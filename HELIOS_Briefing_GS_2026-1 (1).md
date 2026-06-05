# 🛰️ HÉLIOS — Briefing do Projeto

### Global Solution 2026.1 · FIAP · Tema: Indústria Espacial

> Documento para alinhar o grupo. Contém **a ideia** e **o que cada matéria entrega**, tudo conectado num único produto.

---

## 1. A ideia em uma frase

> **HÉLIOS** é um sistema autônomo com IA que **percebe quando um painel solar gera menos energia, diagnostica a causa cruzando vários sensores (não só a câmera) e — se for sujeira — limpa sozinho por vibração, sem água e sem contato** — mantendo a energia de uma base lunar, com um painel de controle que mostra tudo em tempo real.

**Por que existe:** na Lua, a poeira eletrostática cobre os painéis solares e derruba a geração de energia. Não há gente sobrando para limpar, e a distância Terra–Lua (~1,3 segundo-luz) torna o controle remoto inviável. **A decisão precisa acontecer no local, sozinha.** É isso que justifica IA na borda (edge AI).

**Custo zero de hardware:** fazemos **tudo local e simulado**. A única coisa física é a **webcam** (exigência do Physical Computing). Os sensores, a vibração e a base lunar são um **gêmeo digital na tela** que reage de verdade ao que a câmera vê.

---

## 2. A demo (o "filme" que vamos gravar/apresentar)

Loop fechado, ao vivo, atravessando o sistema inteiro:

1. No app, o **gráfico de energia de um painel começa a cair** (gatilho).
2. O HÉLIOS **investiga a causa**: cruza energia + visão (webcam) + condições do ambiente.
3. Jogamos pó/papel no "painel" na frente da webcam → a **visão confirma sujeira**.
4. Diagnóstico = **SUJEIRA** → o HÉLIOS **modula a vibração**; no gêmeo digital o painel vibra e a poeira se solta.
5. A **energia recupera ao vivo** e o **alerta se resolve sozinho**.

Bônus de impacto: dá pra mostrar um caso em que a energia cai mas a visão **não** vê sujeira (ex.: trinca) → o sistema **não vibra** e abre alerta de manutenção. Isso prova que ele *pensa*, não só reage.

O objetivo não é "tirar 10" — é o avaliador pedir **"mostra de novo"** e perguntar **"quando vocês embarcam isso?"**.

---

## 3. O cérebro — diagnóstico por fusão de sensores

**O gatilho é a energia, não a câmera.** É assim que a indústria solar real detecta sujeira: comparando a energia que o painel *deveria* gerar com a que ele *está* gerando (o chamado "soiling ratio" / "performance ratio"). A queda é o **sintoma**.

**O problema:** energia caindo é **ambígua** — pode ser sujeira, sombra, trinca, frio, sol baixo ou defeito elétrico. Por isso o HÉLIOS **cruza os sinais** (fusão de sensores) para descobrir a causa antes de agir. Isso também resolve a fragilidade de depender só de visão.

**Matriz de diagnóstico:**

| Sintoma (energia) | O que a visão vê | Outros sinais | Diagnóstico | Ação |
|---|---|---|---|---|
| Queda **gradual** | cobertura de poeira | sol/temp normais | **SUJEIRA** | vibrar (modulado) |
| Queda **súbita** | nada / sombra parcial | — | **SOMBRA** | esperar, não agir |
| Queda + ponto quente | **trinca/dano** | — | **DANO FÍSICO** | alerta de manutenção, **NÃO vibrar** |
| Queda | painel limpo | sol baixo / frio | **AMBIENTAL** | normal, sem ação |
| Queda | painel limpo | sol/temp normais | **FALHA ELÉTRICA** | alertar humano |

> O HÉLIOS deixa de "ver sujeira" e passa a **diagnosticar**: detecta pela energia, investiga a causa e só aciona a vibração se for realmente sujeira — senão poderia, por exemplo, piorar um painel já trincado.

---

## 4. O mecanismo de limpeza — vibração + IA em malha fechada

**Como limpa (quando o diagnóstico é SUJEIRA):** por **vibração, sem água e sem contato**. Atuadores piezoelétricos vibram o painel e desalojam a poeira, que escorrega pela inclinação.

**Por que NÃO escova:** a poeira lunar é abrasiva — escova risca o vidro, pode trincar o painel e não remove as partículas mais finas.

**Por que NÃO jato d'água:** funciona na Terra, mas é o método *velho* (fazendas solares ficam em desertos, onde água é escassa); na Lua é proibitivo (água ≈ combustível). A vibração **soma o ODS 6 (água)** aos ODS 7 e 13.

**Malha fechada (o diferencial):**
1. **Mede a sujeira** pela visão (% de cobertura).
2. **Modula a vibração** proporcional à sujeira (não é liga-desliga).
3. **Verifica** se a energia recuperou; se não, ajusta e repete.
4. **Prevê** a curva de acúmulo e decide o melhor momento de limpar, poupando energia e o atuador.

> **Validação técnica (real):** patente com piezo + sensores que monitoram o acúmulo e acionam a vibração; pesquisa de 2025 com filmes piezoelétricos de PVDF (limpeza sem água); testes com atuadores piezocerâmicos restauraram até ~95% da geração. (Fontes na seção 13.)

**Uso dual na Terra:** limpeza *sem água* de fazendas solares e infraestrutura em regiões áridas — mercado real e em crescimento.

---

## 5. Por que estoura na curva

- **Sistema real, não tela mockada:** as peças (sensores → diagnóstico → ação → app) conversam de verdade, local, fechando o ciclo **perceber → diagnosticar → agir → verificar**.
- **Ele pensa, não só reage:** fusão de sensores + motor de diagnóstico, não um "vibrador burro" nem um escudo passivo.
- **Arquitetura edge-ready:** o cérebro é um agente isolado, pronto para embarcar num ESP32/Pi depois.
- **ODS atendidos:** 6 (água), 7 (energia limpa), 9 (inovação/infra), 11 (cidades sustentáveis), 13 (clima).

---

## 6. Como tudo se conecta (visão geral)

```
  [SENSOR DE ENERGIA] --(queda?)--> GATILHO
        |
        v
  [MOTOR DE DIAGNÓSTICO]  <---- [WEBCAM/visão: grau de sujeira]
        |                  <---- [sol, temperatura, etc.]
        |
        +--> causa = SUJEIRA --> [VIBRAÇÃO modulada] --> energia recupera
        +--> causa = DANO/FALHA --> [ALERTA p/ humano]
        |
        v
  Serviços Java (SOA) <---> Domínio C# (.NET: Sensor / MotorDiagnostico /
        |                    Causa / AtuadorVibracao / TarefaLimpeza)
        v
  App Mobile (React Native)  +  Gêmeo digital (painel vibra, poeira sai)

  Tudo protegido por --> Cybersecurity
  Tudo mapeado em     --> QA (ArchiMate + Backlog)
```

---

## 7. Contrato de dados compartilhado (a cola entre as matérias)

Todas as matérias usam **o mesmo formato de JSON**. Mesmo com dados simulados, fica coerente.

```json
// Leitura de sensor de energia (o gatilho)
{ "sensorId": "PANEL-A-POWER", "tipo": "potencia", "valor": 142.0,
  "esperado": 210.0, "unidade": "W", "ativoId": "PAINEL-A",
  "timestamp": "2026-06-01T14:03:00Z" }

// Grau de sujeira (saída da visão)
{ "ativoId": "PAINEL-A", "grauSujeira": 0.47, "fonte": "visao",
  "timestamp": "2026-06-01T14:03:00Z" }

// Diagnóstico (saída do motor de diagnóstico)
{ "ativoId": "PAINEL-A", "causa": "SUJEIRA", "confianca": 0.88,
  "evidencias": ["queda_gradual", "cobertura_0.47", "ambiente_normal"],
  "timestamp": "2026-06-01T14:03:01Z" }

// Alerta
{ "alertaId": "ALT-001", "ativoId": "PAINEL-A", "severidade": "ALTA",
  "tipo": "SUJEIRA_DETECTADA", "mensagem": "Perda de 32% por sujeira",
  "timestamp": "2026-06-01T14:03:01Z", "resolvido": false }

// Comando de limpeza (vibração modulada)
{ "comandoId": "CMD-77", "atuadorId": "VIB-PAINEL-A", "acao": "VIBRAR",
  "intensidade": 0.7, "duracaoSeg": 8, "alvoAtivoId": "PAINEL-A",
  "timestamp": "2026-06-01T14:03:02Z" }
```

---

## 8. Resumo das stacks

| Matéria | Stack | Papel no HÉLIOS |
|---|---|---|
| Physical Computing | Python (OpenCV/YOLO/MediaPipe/EasyOCR) | Os "olhos" — mede o grau de sujeira (1 das evidências) |
| Mobile Dev & IoT | React Native + Expo | Painel de controle ao vivo |
| SOA | Java *(confirmar brief)* | Serviços que conectam o loop |
| C# | .NET (Console/API) | Motor de diagnóstico, entidades e controle da vibração |
| Cybersecurity | Documento de análise | Blindagem do sistema |
| QA | ArchiMate + Backlog | Planta e planejamento |

---

## 9. Entregas por matéria

### 9.1 Physical Computing — IoT & IoB · ⭐ começar por aqui

**O que a matéria pede:** solução de visão computacional via webcam, em **Python**. Vídeo (50), Script (30), Repositório Git (20).

**O que entregamos:** o módulo "olhos do HÉLIOS" — a webcam mede o **grau de sujeira** (% de cobertura), que entra como **evidência** no diagnóstico (não como verdade única).

**Como fazer (pipeline):**
1. Captura da webcam (OpenCV) num loop eficiente.
2. Medição: começar com OpenCV (diferença de cor/área → "% de cobertura") e evoluir para **YOLO** (detectar trinca/objeto, útil pro diagnóstico de DANO).
3. Pós-processamento: gerar `grauSujeira` (0–1).
4. Saída: envia `grauSujeira` no formato do contrato (seção 7).
5. **Robustez (vale nota):** funcionar sob luz fraca, ruído e oclusão — e **mostrar isso no vídeo**. (A fusão com energia já reforça a robustez do sistema.)
6. **Tratamento de exceção obrigatório:** webcam desconectada, queda de frame.

**Repositório (20 pts):** público, `requirements.txt`, README completo (descrição + diagrama, objetivo negócio/técnico, bibliotecas, pipeline, execução, **nomes dos integrantes**).

**Critérios:** Vídeo 50 · Script 30 (modularização, loop eficiente, comentários, exceções) · Git 20.

**Depende de:** nada (gera o `grauSujeira`).

---

### 9.2 Mobile Development & IoT

**O que a matéria pede:** app em **React Native + Expo** como **dashboard central** da GS. (Web responsiva mobile-first é alternativa aceita.)

**Telas (Expo Router — tabs + stack):**
1. **Login do Operador** — formulário com validação (representa Cyber).
2. **Energia & Diagnóstico** *(dashboard 1)* — gráfico de energia (real vs esperado) + a causa diagnosticada de cada painel.
3. **Limpeza / Vibração** — status: comando, intensidade, energia recuperada (representa Physical Computing).
4. **Alertas** *(dashboard 2)* — lista com severidade + "reconhecer"; gráfico por tipo de causa.
5. **Configurações** — tema claro/escuro + preferências.

**Requisitos obrigatórios (mapa):**
- Expo Router 3+ telas ✔ · useState/useEffect em 2+ componentes ✔ · Context API (operador + tema + dados) ✔ · AsyncStorage lendo **e** escrevendo ✔ · Formulário com validação (login) ✔ · 2+ dashboards com gráficos (Victory Native / Chart Kit) ✔ · Componentização (`SensorCard`, `DiagnosticoCard`, `AlertItem`) ✔ · UI tema espacial ✔.

**Diferenciais (5 pts):** TypeScript · API real (NASA / OpenWeatherMap) · tema claro/escuro · animações (Moti) · QR Code via Expo Go.

**Entrega:** `entrega.txt` (nome + RM + 3 links), repo GitHub público, README completo (descrição + prints + instruções), vídeo ≤ 3 min.

**Critérios (0–100):** Funcionalidade 30 · Uso dos recursos 25 · Dashboards 15 · Qualidade de código 15 · Conexão com o tema 10 · Diferenciais 5.

**Depende de:** consome o contrato (seção 7) — roda com dados simulados.

---

### 9.3 SOA — Service-Oriented Architecture (Java)

> ⚠️ **Confirmar o brief** — escopo padrão de microsserviços; ajustar com o documento oficial.

**Serviços sugeridos (Java / Spring Boot, REST):**
- **Auth / Identidade** — login, papéis, acesso.
- **Ingestão de Telemetria** — recebe energia, grau de sujeira e ambiente.
- **Diagnóstico** — cruza os sinais e classifica a causa.
- **Motor de Alertas** — regras → alertas.
- **Controle de Limpeza** — se causa = SUJEIRA, calcula a vibração e despacha; registra histórico.
- **Histórico / Relatórios** — leituras e comandos com timestamp.
- **API Gateway**.

**Comunicação:** REST + broker (MQTT/RabbitMQ) para eventos em tempo real.

---

### 9.4 C# — .NET

**O que a matéria pede:** projeto .NET (Console/WinForms/WPF ou API Core) com POO sólida.

**O que entregamos:** o **cérebro** do HÉLIOS — entidades, o **motor de diagnóstico** (fusão de sensores, polimorfismo das causas) e a **lógica de vibração modulada**.

**Modelagem (entidades):**
- `Ativo` / `PainelSolar`
- `Sensor` *(abstrata)* → `SensorEnergia`, `SensorCorrente`, `SensorTemperatura`, `SensorLuz`
- `Leitura` *(com DateTime)*
- `Causa` *(abstrata)* → `Sujeira`, `Sombra`, `DanoFisico`, `FalhaEletrica`, `Ambiental`
- `MotorDiagnostico`
- `AtuadorVibracao`, `TarefaLimpeza`, `Alerta`

**Trechos-chave:**
```csharp
// Diagnóstico por fusão de sinais (polimorfismo nas causas)
public Causa Diagnosticar(LeituraEnergia e, double grauSujeira, Ambiente amb)
{
    if (!e.HouveQueda())                 return new SemFalha();
    if (amb.SolBaixo || amb.Frio)        return new Ambiental();
    if (grauSujeira >= 0.15 && e.QuedaGradual) return new Sujeira(grauSujeira);
    if (e.PontoQuente)                   return new DanoFisico();
    if (grauSujeira < 0.05)              return new FalhaEletrica();
    return new Sombra();
}

// Vibração modulada (só se a causa for Sujeira)
public ComandoVibracao CalcularIntensidade(double grauSujeira)
{
    double intensidade = Math.Clamp(grauSujeira, 0.3, 1.0);
    return new ComandoVibracao(intensidade, (int)(intensidade * 12));
}
```

**Itens obrigatórios (mapa de pontos):**
- **Modelagem & POO (20):** herança (`Sensor`, `Causa`), polimorfismo no diagnóstico.
- **Abstração & Interfaces (20):** `Sensor`/`Causa` abstratas; `ISensor`, `IRepositorio<T>`, `INotificador`, `IPoliticaLimpeza` + injeção de dependência.
- **Lógica, Métodos & Datas (15):** `Diagnosticar`/`CalcularIntensidade`; `DateTime` para histórico e curva de sujeira.
- **Tratamento de Exceções (10):** sensor offline, leitura inválida, atuador sem resposta.
- **Structs/Partial (5):** `struct LeituraValor`/`Coordenada`; classes partial.
- **Organização (30):** pastas/nomenclatura; **README (motivação + integração)**; **diagrama de fluxo**; **evidências de execução (obrigatório)**.

---

### 9.5 Cybersecurity

**O que a matéria pede:** segurança da solução, pensando como atacante (Red Team) e defensor (Blue Team); extensão da proposta. (10 pts.)

**1. Threat Modeling**
- *Ativos:* link de comunicação, base de telemetria, **canal de comando da vibração**, **sensores (energia/visão/ambiente)**, credenciais, firmware do agente de borda.
- *Vetores (≥3):* (a) **spoofing do sensor de energia** — fingir que está tudo bem (painel nunca limpa) ou forjar queda (forçar vibração); (b) **manipulação do feed de visão** (falsear o grau de sujeira → diagnóstico errado); (c) **injeção de comando** para vibração excessiva e desgaste do atuador; (d) DDoS no serviço de diagnóstico/controle; (e) replay de comandos.

**2. Arquitetura de Segurança**
- *Acesso:* MFA + privilégio mínimo + RBAC.
- *Dados:* mTLS + criptografia em repouso; **assinatura de comando + anti-replay** (nonce/timestamp); **validação cruzada de sensores** (a própria fusão dificulta o spoofing de um único sensor); limites físicos no firmware.
- *Infra:* Zero Trust, monitoramento de logs, integridade de firmware.

**3. Governança/Compliance:** ISO 27001 (gestão de riscos); LGPD/privacidade (se a câmera captar rosto = dado biométrico).

**4. Resiliência (Resposta a Incidentes):** contenção (revogar sessão, cortar comando, atuador em modo seguro); erradicação; recuperação.

**Critérios:** aplicabilidade, profundidade técnica, coerência.

---

### 9.6 QA — Testing, Compliance & Quality Assurance

**O que a matéria pede:** **ArchiMate (40)** + **Product Backlog (40)** + **Vídeo pitch ≤ 3 min (20)**. Entrega em `.ZIP` (`.archi`, PDF arquitetura, PDF backlog, vídeo). **Prazo: 09/06 no Teams.**

**ArchiMate (4 camadas):** Motivação (stakeholders/requisitos) · Negócio (processo "perceber → diagnosticar → agir → verificar") · Aplicação (visão, diagnóstico, serviços Java, domínio C#, app) · Tecnologia (webcam, broker, banco, APIs, agente de borda).

**Product Backlog (exemplos):**
- *Épicos:* Telemetria de Energia · Medição por Visão · **Diagnóstico** · Limpeza por Vibração · Alertas · Segurança · App.
- *Histórias:*
  - "Como **operador**, quero **ver energia real vs esperada por painel**, para **detectar perdas cedo**."
  - "Como **sistema**, quero **diagnosticar a causa da queda de energia cruzando sensores**, para **agir certo (e não vibrar um painel trincado)**."
  - "Como **sistema**, quero **modular a vibração conforme a sujeira**, para **limpar gastando o mínimo**."
  - "Como **sistema**, quero **prever a curva de sujeira**, para **limpar no melhor momento**."
- Cada história com **critérios de aceite**, **prioridade** e **story points**.

**Pitch (≤3 min):** nome · problema · solução · relação com a indústria espacial · benefício.

---

## 10. Divisão de trabalho sugerida (grupo de até 5)

| Pessoa | Foco principal | Apoio |
|---|---|---|
| 1 | Physical Computing (visão) | C# (entidades) |
| 2 | Mobile (app) | QA (pitch) |
| 3 | SOA (Java) | Cyber |
| 4 | C# (.NET — diagnóstico) | Physical Computing |
| 5 | QA (ArchiMate + backlog) | Cyber |

> Todos contribuem no **vídeo final** e revisam o **contrato de dados** (seção 7).

---

## 11. Prazos conhecidos

- **QA** e **C#**: 09/06 (Teams).
- **Mobile**, **Physical Computing**, **SOA**, **Cyber**: confirmar data na plataforma.

> ✅ Conferir todos os prazos oficiais e marcar no calendário do grupo.

---

## 12. Checklist geral

- [ ] Contrato de dados (seção 7) acordado por todos
- [ ] Repositórios GitHub criados, **públicos**
- [ ] Nomes completos + RM em todos os READMEs/entregas
- [ ] Identidade visual única (HÉLIOS) em app, repos e slides
- [ ] Vídeos ≤ 3 min onde exigido
- [ ] Demo do loop ao vivo ensaiada e gravada por último
- [ ] Conexão com a Indústria Espacial + ODS explícita em cada entrega

---

## 13. Fontes (para embasar pitch e READMEs)

**Detecção de sujeira pela energia (soiling/performance ratio):** prática padrão de O&M solar — compara geração real vs esperada para inferir perda por sujeira.

**Mecanismo de vibração (auto-limpeza sem água):**
- Manzo et al. (2025), *A novel solar panel self-cleaning method based on piezoelectric films* — J. Renewable and Sustainable Energy. DOI: 10.1063/5.0242347
- AIP Scilight (resumo): https://pubs.aip.org/aip/sci/article/2025/3/031101/3330692/
- *Vibration Characterization of Self-Cleaning Solar Panels with Piezoceramic Actuation* (até ~95% de recuperação) — ResearchGate
- Patente US 9415428 — *Methods and systems for self-cleaning of photovoltaic panels* (piezo + sensores): https://patents.google.com/patent/US9415428

**Poeira lunar e mitigação:**
- NASA — EDS testado na missão Blue Ghost 1 (mar/2025): https://www.nasa.gov/
- NASA CLPS / Electrodynamic Dust Shield: https://www.nasa.gov/missions/artemis/clps/

**Mercado/perdas (uso dual na Terra):** soiling causa ~15–35% de perda anual em usinas; mercado de robôs de limpeza ~US$ 621 mi (2021), ~11,5% a.a. até 2032.

> Confirmar e completar links/DOIs ao citar no relatório final.

---

*HÉLIOS — da poeira lunar à energia limpa na Terra.* 🌒→🌍
