// Tipo para membros da equipe
export interface TeamMember {
  id: number;
  name: string;
  role: string;
  image: string;
  linkedin?: string;
  github?: string;
}

// Tipo para perguntas frequentes
export interface FAQItem {
  id: number;
  question: string;
  answer: string;
}

// Tipo para features
export interface Feature {
  id: number;
  title: string;
  description: string;
  icon: string;
  link?: string;
}

// Tipo para item de navegação
export interface NavItem {
  id: number;
  label: string;
  href: string;
  hasDropdown?: boolean;
  dropdownItems?: DropdownItem[];
}

// Tipo para item de dropdown
export interface DropdownItem {
  id: number;
  label: string;
  href: string;
  description?: string;
}

// Tipos para requisições de API
export interface XPathRequest {
  errorXpath: string;
  pageDOM: string;
}

export interface CodeReviewRequest {
  code: string;
  language: string;
}

export interface AutomationRequest {
  description: string;
  framework: string;
}

export interface MigrationRequest {
  code: string;
  componentType: string;
}

// Tipos para respostas da API
export interface XPathResponse {
  newXpath: string;
}

export interface ApiResponse {
  success: boolean;
  message?: string;
  data?: any;
  error?: string;
} 