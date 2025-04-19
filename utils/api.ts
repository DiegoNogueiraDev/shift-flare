import { 
  XPathRequest, 
  XPathResponse, 
  CodeReviewRequest,
  AutomationRequest,
  MigrationRequest,
  ApiResponse 
} from '../types';

const API_BASE_URL = '/api';

// Função auxiliar para fazer requisições fetch
async function fetchData<T>(url: string, method: string, data?: any): Promise<T> {
  const options: RequestInit = {
    method,
    headers: {
      'Content-Type': 'application/json',
    },
    ...(data && { body: JSON.stringify(data) }),
  };

  const response = await fetch(`${API_BASE_URL}${url}`, options);

  if (!response.ok) {
    // Tenta extrair a mensagem de erro da resposta
    try {
      const errorData = await response.json();
      throw new Error(errorData.message || `Erro ${response.status}: ${response.statusText}`);
    } catch (e) {
      throw new Error(`Erro ${response.status}: ${response.statusText}`);
    }
  }

  return response.json();
}

// Função para predição de XPath
export async function getXPathCorrection(input: XPathRequest): Promise<XPathResponse> {
  return fetchData<XPathResponse>('/v1/xpath/predict', 'POST', input);
}

// Função para análise de código
export async function getCodeReview(input: CodeReviewRequest): Promise<ApiResponse> {
  return fetchData<ApiResponse>('/ai/code-review', 'POST', input);
}

// Função para geração de scripts de automação
export async function generateAutomation(input: AutomationRequest): Promise<ApiResponse> {
  return fetchData<ApiResponse>('/ai/automation', 'POST', input);
}

// Função para migração de código Q2 para Q3
export async function migrateCode(input: MigrationRequest): Promise<ApiResponse> {
  return fetchData<ApiResponse>('/ai/migration', 'POST', input);
}

// Função para testar a conexão com o OpenRouter
export async function testOpenRouterConnection(): Promise<ApiResponse> {
  return fetchData<ApiResponse>('/test/test-openrouter-connection', 'GET');
}

// Função para executar todos os cenários de teste
export async function runAllTestScenarios(): Promise<ApiResponse> {
  return fetchData<ApiResponse>('/test/run-all', 'GET');
} 