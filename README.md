# 🛒 GroceryList

Aplicativo Android para criar e gerenciar listas de compras, com sincronização dos dados em tempo real.

Projeto desenvolvido para a disciplina de **Gerenciamento de Conteúdo Mobile** do curso de **Análise e Desenvolvimento de Sistemas** da **Etep**, ministrada pelo professor **Lucas Renan**.

## Funcionalidades

- Criar listas de compras com nome personalizado
- Adicionar itens a uma lista, informando nome e quantidade
- Atualizar a quantidade de um item
- Sincronização dos dados em tempo real com o Firebase

## Tecnologias

- [Android Studio](https://developer.android.com/studio)
- [Kotlin](https://kotlinlang.org/)
- [Firebase Realtime Database](https://firebase.google.com/docs/database)

## Estrutura dos dados

Os dados ficam armazenados no Realtime Database no seguinte formato:

```
listas
└── {listaId}
    ├── name: "Lista 1"
    └── items
        └── {itemId}
            ├── name: "Pão"
            └── amount: 2
```

## Como executar

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   ```
2. Abra o projeto no Android Studio.
3. Crie um projeto no [Firebase Console](https://console.firebase.google.com/) e ative o **Realtime Database**.
4. Registre o app Android no Firebase, baixe o arquivo `google-services.json` e coloque-o na pasta `app/`.
5. Sincronize o Gradle e execute o app em um emulador ou dispositivo físico.

## Autores

Desenvolvido por:

**Rafael Poddis Busquim e Silva**

**Marcelo Ramos Gomes**
