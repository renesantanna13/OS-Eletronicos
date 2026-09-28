# OS Tracker

Aplicativo Android para acompanhar as ordens de serviço (OS) de uma assistência técnica de eletrônicos. A ideia é que o técnico consiga ver todos os aparelhos que estão na bancada e acompanhar o andamento de cada reparo, etapa por etapa.

Projeto da disciplina **Programação Mobile 1** — entrega parcial (Android Views com XML + navegação por Intent).

## Funcionalidades

- **Lista de OS (`HomeActivity`)**: mostra os aparelhos em atendimento com número da OS, cliente, status, se é urgente e o progresso do reparo. A lista usa `RecyclerView`.
- **Detalhe da OS (`OrderDetailActivity`)**: abre ao tocar em um item da lista. Mostra o problema relatado, o diagnóstico, o checklist de etapas e os dados do atendimento.
- **Card de detalhes (`OrderInfoFragment`)**: fragment dentro da tela de detalhe com os dados do cliente e do atendimento, e um botão para ligar para o cliente, que abre o discador com o número. O botão some quando a OS não tem telefone.
- **Andamento do reparo**: os botões avançam ou desfazem uma etapa. A barra de progresso, o status e o checklist atualizam na hora, e ao voltar para a lista o progresso também aparece atualizado.

## Como foi feito

- Duas telas em XML usando `ScrollView`, `LinearLayout`, `FrameLayout` e `RecyclerView`
- Navegação com `Intent` explícita: a lista envia o `id` da OS para a tela de detalhe, que busca os dados no repositório. Se o id não existir, a tela mostra um aviso e fecha.
- `ViewBinding` em todas as telas (sem `findViewById`)
- Modelo imutável com `data class` (`ServiceOrder`). Para avançar uma etapa é criada uma cópia com `copy()`.
- Campos opcionais (telefone, modelo, diagnóstico, técnico, previsão e orçamento) são nullable e têm um texto padrão quando não foram preenchidos
- `Fragment` com ViewBinding (`OrderInfoFragment`):
  - Recebe o id da OS por `arguments`
  - Monta a interface em `onViewCreated`
  - Limpa o binding em `onDestroyView`
  - Só é adicionado quando `savedInstanceState == null`, para não duplicar ao girar a tela
- Dados mockados em `ServiceOrderRepository`, sem API ou banco de dados nesta etapa
- Componentes XML reutilizáveis:
  - `badge_layout`: etiqueta de status, categoria e urgência
  - `detail_row_layout`: linha de título e valor
  - `step_item_layout`: etapa do checklist
  - Os três são usados com `<include>` ou inflados pelo Kotlin

## Como rodar

1. Clone o repositório:
   ```
   git clone https://github.com/renesantanna13/OS-Eletronicos.git
   ```
2. Abra a pasta no **Android Studio** (File > Open) e espere o Gradle sincronizar
3. Selecione um emulador ou um celular com Android 13 (API 33) ou superior
4. Clique em **Run**

## Bibliotecas

O projeto não usa bibliotecas de terceiros, só as do próprio Android:

- **AndroidX AppCompat / Core KTX / Activity KTX**: base das Activities e funções como `enableEdgeToEdge()`
- **AndroidX Fragment** (vem junto com o AppCompat): `Fragment` e `FragmentContainerView`
- **Material Components**: `MaterialToolbar`, `FloatingActionButton`, `LinearProgressIndicator`, `MaterialDivider` e tema Material 3
- **RecyclerView** (vem junto com o Material): lista de ordens de serviço
- **Jetpack Compose**: já configurado no Gradle para a próxima etapa da disciplina, mas ainda não é usado nas telas
