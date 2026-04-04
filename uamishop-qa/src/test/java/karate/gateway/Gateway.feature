Feature: Pruebas centralizadas del API Gateway

  Background:
    * url 'http://localhost:8080'
    * header Authorization = 'Bearer token-super-secreto-qa'

  Scenario: Verificar que el Gateway enruta a Catálogo y trae las categorias
    Given path '/api/v1/catalogo/categorias'
    When method get
    Then status 200
    And match response == '#array'