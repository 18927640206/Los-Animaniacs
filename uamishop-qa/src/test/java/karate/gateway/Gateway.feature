Feature: Pruebas centralizadas del API Gateway

  Background:
    * url 'http://localhost:8080'
    * header Authorization = 'Bearer token-super-secreto-qa'

  Scenario: Verificar que el Gateway enruta a Catálogo y trae las categorias
    Given path '/api/v1/catalogo/categorias'
    When method get
    Then status 200
    And match response == '#array'

  Scenario: Agregar item al carrito (Creación de carrito)
    Given path '/api/v1/carritos'
    And request { clienteId: "11111111-1111-1111-1111-111111111111" }
    When method post
    Then status 201
    * def carritoCreadoId = response.id
    * print 'El ID del carrito creado es: ', carritoCreadoId

  Scenario: Crear orden (con outbox simulado)
    Given path '/api/v1/carritos'
    And request { clienteId: "11111111-1111-1111-1111-111111111111" }
    When method post
    Then status 201
    * def carritoActualId = response.id

    # -- AQUÍ ESTÁ EL ARREGLO: Volvemos a declarar el token --
    * header Authorization = 'Bearer token-super-secreto-qa'
    
    Given path '/api/v1/ordenes'
    And request 
    """
    {
      "clienteId": "11111111-1111-1111-1111-111111111111",
      "carritoId": "#(carritoActualId)",
      "items": [
        {
          "productoId": "22222222-2222-2222-2222-222222222222",
          "cantidad": 1,
          "precioUnitario": 150.00
        }
      ],
      "direccionEnvio": {
        "calle": "Av. Universidad 123",
        "colonia": "Iztapalapa",
        "ciudad": "CDMX",
        "estado": "Ciudad de México",
        "codigoPostal": "09340",
        "pais": "México",
        "telefono": "5512345678"
      }
    }
    """
    When method post
    Then status 201
    And match response.estado == 'PENDIENTE'