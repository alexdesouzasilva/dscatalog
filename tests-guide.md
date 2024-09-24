# Tipos de testes


## Unitário
* Feitos pelo desenvolvedor.
* Objetivo: validar comportamentos de unidades funcionais do código, ou seja, avaliar comportamento dos métodos das classes de forma isolada.
* Não pode instanciar outros objetos reais, deverá ser usado o mock.
* Não pode acessar outros componentes: (arquivos, bd, rede, web services, etc)


## Integração
* Verificar componentes/módulos da aplicação, e também recursos externos.


## Funcional
* Teste do ponto de vista do usuário.
* Ex: Ao digitar o número da agência, deverá retornar o número de todas as contas.


# Benefícios dos testes
* Detectar facilmente se mudanças violaram regras.
* É uma forma de documentação.
* Redução de custo de manutenção.
* Melhora design da aplicação.


# TDD - Test Driven Development
### Desenvolvimento guiado pelos testes.
* Método de desenvolver softwares.
* Primeiro escreve o teste, depois desenvolve as funcionalidades.


## Processo básico
* Escrever o teste como esperado.
* Implementar o código necessário para que o teste passe.
* Refatorar o código conforme necessidade.


# Boas práticas e padrões

### Nomenclatura de um teste
* <AÇÃO> should <EFEITO> [when <CENÁRIO>]

### Padrão AAA
* Arrange: instancie os objetos necessários.
* Act: execute as ações necessárias.
* Assert: declare o que deveria acontecert (resultado esperado)

### Princípio da inversão de dependência (SOLID)
* Se uma classe A depende de uma instância da classe B, não tem como testar a classe A isoladamente. Na verdade nem seria um teste unitário.
* A inversão de controle ajuda na testabilidade, e grante o isolamento da unidade a ser testada.

### Independência / Isolamento
* Um teste não pode depender de outros testes, nem da ordem de execução.


### Cenário único.
* O teste deve ter uma lógica simples, linear.
* O teste deve testar apenas um cenário, ou seja, um teste para consultar quando o ID existe e outro para testar quando o ID não existe.


### Pervisibilidade
* O reasultado de um teste deve ser sempre o mesmo para os mesmos dados.
* Não faça o resultado depender de coisas que variam, tais como timestamp atual e valores aleatórios.

# Junit
### Boas práticas
* doc: https://junit.org/junit5
1. Criar classe de teste
2. Os métodos de testes deverão ser anotados com @Test
3. O método com @Test deverá ser void.
4. Todos métodos deverão passar sem falhas.
5. O que define se um método de teste passou ou não são as ***assertions***
