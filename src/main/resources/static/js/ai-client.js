/**
 * Cliente JavaScript para comunicação com os endpoints de IA.
 * Este arquivo fornece funções para interação com as APIs Spring Boot.
 */

/**
 * Realiza uma análise de código (Code Review).
 * 
 * @param {string} code - O código a ser analisado
 * @param {string} language - A linguagem de programação
 * @returns {Promise} - Promise com o resultado da análise
 */
async function analyzeCode(code, language = 'auto') {
    try {
        const response = await fetch('/api/ai/code-review', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                code: code,
                language: language
            })
        });
        
        if (!response.ok) {
            throw new Error(`Erro ao analisar código: ${response.status} ${response.statusText}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Erro na análise de código:', error);
        throw error;
    }
}

/**
 * Gera script de automação baseado em uma descrição.
 * 
 * @param {string} description - Descrição do processo a ser automatizado
 * @param {string} framework - O framework de automação a ser utilizado
 * @returns {Promise} - Promise com o resultado da automação
 */
async function generateAutomation(description, framework = 'selenium') {
    try {
        const response = await fetch('/api/ai/automation', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                description: description,
                framework: framework
            })
        });
        
        if (!response.ok) {
            throw new Error(`Erro ao gerar automação: ${response.status} ${response.statusText}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Erro na geração de automação:', error);
        throw error;
    }
}

/**
 * Realiza migração de código Q2 para Q3.
 * 
 * @param {string} code - O código Q2 a ser migrado
 * @param {string} componentType - O tipo de componente 
 * @returns {Promise} - Promise com o resultado da migração
 */
async function migrateCode(code, componentType = 'autodetect') {
    try {
        const response = await fetch('/api/ai/migration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                code: code,
                componentType: componentType
            })
        });
        
        if (!response.ok) {
            throw new Error(`Erro ao migrar código: ${response.status} ${response.statusText}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Erro na migração de código:', error);
        throw error;
    }
}

/**
 * Função para processar a resposta da API e formatar para exibição.
 * 
 * @param {Object} response - A resposta da API
 * @returns {Object} - Objeto formatado para exibição no frontend
 */
function processApiResponse(response) {
    if (!response.success) {
        return {
            success: false,
            error: response.error || 'Ocorreu um erro inesperado'
        };
    }
    
    if (!response.response) {
        return {
            success: false,
            error: 'Resposta vazia do servidor'
        };
    }
    
    return {
        success: true,
        content: response.response
    };
} 