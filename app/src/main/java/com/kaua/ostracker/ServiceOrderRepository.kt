package com.kaua.ostracker

// Dados mockados em memoria (sem API/banco nesta etapa)
object ServiceOrderRepository {

    private val defaultSteps = listOf(
        "Triagem e testes iniciais",
        "Diagnóstico técnico",
        "Aprovação do orçamento",
        "Reparo / troca de peças",
        "Testes finais",
        "Limpeza e embalagem"
    )

    private val orders = mutableListOf(
        ServiceOrder(
            id = "1",
            number = 1042,
            deviceName = "Galaxy S23",
            brand = "Samsung",
            model = "SM-S911B",
            category = DeviceCategory.SMARTPHONE,
            customerName = "Mariana Souza",
            customerPhone = "(11) 98765-4321",
            reportedIssue = "Tela trincada após queda e touch não responde na parte de baixo.",
            diagnosis = "Display AMOLED danificado. Necessária troca do conjunto tela + frame.",
            technician = "Kaua",
            receivedAt = "18/09/2026",
            estimatedDelivery = "26/09/2026",
            estimatedCost = 1250.0,
            steps = defaultSteps,
            completedSteps = 3,
            isUrgent = true
        ),
        ServiceOrder(
            id = "2",
            number = 1043,
            deviceName = "Ideapad 3",
            brand = "Lenovo",
            model = null,
            category = DeviceCategory.NOTEBOOK,
            customerName = "Carlos Henrique",
            customerPhone = null,
            reportedIssue = "Não liga. O LED de carga pisca e apaga.",
            diagnosis = null,
            technician = null,
            receivedAt = "22/09/2026",
            estimatedDelivery = null,
            estimatedCost = null,
            steps = defaultSteps
        ),
        ServiceOrder(
            id = "3",
            number = 1044,
            deviceName = "PlayStation 5",
            brand = "Sony",
            model = "CFI-1214A",
            category = DeviceCategory.VIDEOGAME,
            customerName = "Rafael Lima",
            customerPhone = "(11) 91234-5678",
            reportedIssue = "Superaquecendo e desligando sozinho depois de 20 minutos de jogo.",
            diagnosis = "Metal líquido deslocado e ventoinha com acúmulo de poeira.",
            technician = "Kaua",
            receivedAt = "15/09/2026",
            estimatedDelivery = "25/09/2026",
            estimatedCost = 380.0,
            steps = listOf(
                "Triagem e testes iniciais",
                "Diagnóstico técnico",
                "Aprovação do orçamento",
                "Desmontagem",
                "Limpeza interna e troca do metal líquido",
                "Teste de estresse (1h)",
                "Montagem e embalagem"
            ),
            completedSteps = 5
        ),
        ServiceOrder(
            id = "4",
            number = 1045,
            deviceName = "Smart TV 50\" 4K",
            brand = "LG",
            model = "50UR8750",
            category = DeviceCategory.TV,
            customerName = "Ana Paula Ribeiro",
            customerPhone = "(11) 99876-1122",
            reportedIssue = "Tem som, mas a imagem fica escura (backlight).",
            diagnosis = "Barras de LED do backlight queimadas.",
            technician = "Juliana",
            receivedAt = "10/09/2026",
            estimatedDelivery = "20/09/2026",
            estimatedCost = 540.0,
            steps = defaultSteps,
            completedSteps = 6
        ),
        ServiceOrder(
            id = "5",
            number = 1046,
            deviceName = "iPhone 13",
            brand = "Apple",
            model = "A2633",
            category = DeviceCategory.SMARTPHONE,
            customerName = "Bruno Tavares",
            customerPhone = "(11) 97777-3030",
            reportedIssue = "Bateria descarregando muito rápido (saúde em 71%).",
            diagnosis = "Bateria degradada. Troca recomendada.",
            technician = "Juliana",
            receivedAt = "21/09/2026",
            estimatedDelivery = "24/09/2026",
            estimatedCost = 420.0,
            steps = listOf(
                "Triagem e testes iniciais",
                "Aprovação do orçamento",
                "Troca da bateria",
                "Calibração e testes"
            ),
            completedSteps = 1
        ),
        ServiceOrder(
            id = "6",
            number = 1047,
            deviceName = "Nintendo Switch",
            brand = "Nintendo",
            model = "OLED",
            category = DeviceCategory.VIDEOGAME,
            customerName = "Letícia Martins",
            customerPhone = null,
            reportedIssue = "Joy-Con esquerdo com drift no analógico.",
            diagnosis = null,
            technician = "Kaua",
            receivedAt = "23/09/2026",
            estimatedDelivery = null,
            estimatedCost = null,
            steps = listOf(
                "Triagem e testes iniciais",
                "Troca do módulo analógico",
                "Calibração e testes"
            )
        ),
        ServiceOrder(
            id = "7",
            number = 1048,
            deviceName = "MacBook Air M1",
            brand = "Apple",
            model = "A2337",
            category = DeviceCategory.NOTEBOOK,
            customerName = "Pedro Almeida",
            customerPhone = "(11) 96543-2109",
            reportedIssue = "Caiu café no teclado. Algumas teclas não funcionam.",
            diagnosis = "Oxidação na placa do teclado. Placa-mãe sem danos.",
            technician = "Juliana",
            receivedAt = "19/09/2026",
            estimatedDelivery = "30/09/2026",
            estimatedCost = 890.0,
            steps = defaultSteps,
            completedSteps = 2,
            isUrgent = true
        )
    )

    fun getAll(): List<ServiceOrder> = orders.toList()

    fun findById(id: String): ServiceOrder? = orders.find { it.id == id }

    fun update(order: ServiceOrder) {
        val index = orders.indexOfFirst { it.id == order.id }
        if (index != -1) {
            orders[index] = order
        }
    }
}
