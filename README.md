## Introdução
O caso de uso consiste em desenvolver uma funcionalidade que recebe um objeto contendo o código do vendedor e uma lista de pagamentos realizados. Cada pagamento é identificado pelo código da cobrança a que ele se refere. O sistema deve validar se o vendedor e o código da cobrança existem na base de dados. Além disso, ele deve verificar se o pagamento é parcial, total ou excedente em comparação com o valor original cobrado. Para cada situação de pagamento, o sistema deve enviar o objeto para uma fila SQS (Simple Queue Service) distinta e retornar o mesmo objeto recebido com a informação do status de pagamento preenchida.

## Tecnologias

- Java 17
- Gradle
- Spring Boot 3.3.4
- Spring Web
- JUnit 5
- AWS SQS
- LocalStack

## Testes

Para executar os testes, no terminal acesse a pasta `san-giorgio-api` e utilize o comando abaixo:
```bash
./gradlew :san-giorgio-api:test :san-giorgio-api:jacocoTestReport
```

Depois é só acessar a pasta o arquivo `index.html` que está na pasta `san-giorgio-api/build/reports/jacoco/test/html` para verificar o coverage.

### Configuração para SQS

Criar recurso SQS na AWS ou LocalStack para poder ter as informações necessárias para
configurar as variáveis de ambiente abaixo:
- AWS_ACCESS_KEY_ID: Chave de acesso da sua conta AWS. Uma string alfanumérica que identifica a sua conta AWS
  usada para autenticar as requisições feitas ao SQS.
- AWS_SECRET_ACCESS_KEY: Chave secreta da sua conta AWS. É uma string alfanumérica usada em conjunto
  com a chave de acesso para autenticar as requisições feitas ao SQS.
- AWS_REGION: Região da AWS onde o recurso SQS está localizado. Por exemplo, se o seu recurso SQS está localizado
  na região "us-east-1", você deve configurar essa variável com o valor "us-east-1".
- AWS_ACCOUNT_ID: ID da sua conta AWS. É um número único que identifica sua conta AWS e é usado para configurar o recurso SQS.

_Observação: as variáveis de ambiente serão carregadas automaticamente no properties do spring através do que foi definido
em `application.yml` disponível em `san-giorgio-api/src/main/resources`._

### Iniciando a aplicação

No terminal, na pasta raiz do projeto, execute o comando:  

```bash
./gradlew :san-giorgio-api:bootRun
```

Localmente disponível no endpoint http://localhost:8080/api/v1/payment

## Requisitos Funcionais

### 1. Receber objeto contendo código do vendedor e lista de pagamentos
- R: Objeto é recebido através do PaymentController e o PaymentRequestDto com validação de campos.

### 2. Validar existência do vendedor
O sistema deve verificar se o vendedor informado no objeto existe na base de dados. Caso não exista, o sistema deve retornar uma mensagem de erro informando que o vendedor não foi encontrado.
- R: Classes ClientService, ClientRepository e ClientEntity utilizadas para validar.

### 3. Validar existência do código da cobrança
Para cada pagamento realizado na lista, o sistema deve verificar se o código da cobrança informado existe na base de dados. Caso não exista, o sistema deve retornar uma mensagem de erro informando que o código da cobrança não foi encontrado.
- R: Classes PaymentService, PaymentRepository e PaymentEntity utilizadas para validar.

### 4. Validar valor do pagamento
O sistema deve comparar o valor do pagamento recebido na requisição com o valor original cobrado, a fim de determinar se o pagamento é parcial, total ou excedente.
- R: Na classe ConfirmPaymentUseCaseImpl tem o método paymentStatus que realiza essa validação.

### 5. Enviar objeto para fila SQS
De acordo com a situação de pagamento (parcial, total ou excedente), o sistema deve enviar o objeto para uma fila SQS distinta. Essa fila será responsável por processar o objeto de acordo com a situação de pagamento.
- R: Criada a interface PaymentQueueUseCase para ser base para o dominio e feita a implementação através da classe SendToSQS.

### 6. Preencher status de pagamento
Após o processamento do objeto, o sistema deve preencher a informação do status de pagamento no mesmo objeto recebido. Essa informação indicará se o pagamento foi parcial, total ou excedente.
- R: No final do processamento da chamada é retornado objeto to tipo PaymentResponseDto.

## Requisitos Não Funcionais
Os requisitos não funcionais descrevem características do sistema que não estão diretamente relacionadas às funcionalidades, mas afetam seu desempenho, segurança, usabilidade, entre outros aspectos.

### 1. Teste unitários
O caso de uso deve ser testavel através de testes unitários.
- R: Criado os testes unitários e adicionado jacoco plugin para geração de relatórios de testes e coverage.

### 2. Tratamento de resposta e status code
O sistema deve retornar uma resposta com status code 200 em caso de sucesso e 4XX em caso de erro.
- R: Se tudo está ok, retorna 200, para caso tenha erro, foram criadas exceptions e configurado exception handler para tratar e desolver a resposta com objeto do tipo ApiErrorDto.


