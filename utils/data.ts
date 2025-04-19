import {FAQItem, Feature, NavItem, TeamMember } from '../types';

// Membros da equipe
export const teamMembers: TeamMember[] = [
  {
    id: 1,
    name: 'Ana Silva',
    role: 'CTO & Co-Fundadora',
    image: '/team/ana-silva.jpg',
    linkedin: 'https://linkedin.com/in/anasilva',
    github: 'https://github.com/anasilva'
  },
  {
    id: 2,
    name: 'Carlos Oliveira',
    role: 'Desenvolvedor Lead',
    image: '/team/carlos-oliveira.jpg',
    linkedin: 'https://linkedin.com/in/carlosoliveira',
    github: 'https://github.com/carlosoliveira'
  },
  {
    id: 3,
    name: 'Juliana Costa',
    role: 'Especialista em QA',
    image: '/team/juliana-costa.jpg',
    linkedin: 'https://linkedin.com/in/julianacosta',
    github: 'https://github.com/julianacosta'
  },
  {
    id: 4,
    name: 'Marcos Santos',
    role: 'Arquiteto de Soluções',
    image: '/team/marcos-santos.jpg',
    linkedin: 'https://linkedin.com/in/marcossantos',
    github: 'https://github.com/marcossantos'
  }
];

// Perguntas frequentes
export const faqItems: FAQItem[] = [
  {
    id: 1,
    question: 'O que é o Shift-Flare?',
    answer: 'Shift-Flare é uma ferramenta avançada de automação e teste para aplicações web, que utiliza inteligência artificial para identificar e corrigir problemas em XPaths e seletores CSS. Ela acelera o desenvolvimento de testes automatizados e aumenta a resiliência dos seus scripts de teste.'
  },
  {
    id: 2,
    question: 'Como o Shift-Flare ajuda a estabilizar testes?',
    answer: 'O Shift-Flare analisa seus seletores CSS e XPaths quando eles falham, sugerindo alternativas mais robustas. Utilizando algoritmos de aprendizado de máquina, ele entende a estrutura do DOM e encontra padrões que tornam seus testes mais resilientes a mudanças na interface.'
  },
  {
    id: 3,
    question: 'O Shift-Flare funciona com qualquer framework de testes?',
    answer: 'Sim, o Shift-Flare foi projetado para ser agnóstico quanto ao framework. Ele pode ser integrado com Selenium, Cypress, Playwright, Puppeteer e outros frameworks populares de automação de testes.'
  },
  {
    id: 4,
    question: 'É possível utilizar o Shift-Flare em ambiente de CI/CD?',
    answer: 'Absolutamente! O Shift-Flare possui uma API que permite integração com pipelines de CI/CD. Você pode configurar seus processos para utilizar automaticamente as sugestões do Shift-Flare quando os testes falham, reduzindo a necessidade de intervenção manual.'
  },
  {
    id: 5,
    question: 'Quanto tempo leva para implementar o Shift-Flare na minha equipe?',
    answer: 'A implementação do Shift-Flare é rápida e pode ser concluída em horas, não dias. Nossa documentação detalhada e suporte técnico especializado garantem que sua equipe estará operacional com a ferramenta em tempo recorde, começando a ver resultados já nos primeiros testes.'
  },
  {
    id: 6,
    question: 'O Shift-Flare pode ser usado por equipes ágeis?',
    answer: 'Perfeitamente! O Shift-Flare foi projetado pensando em equipes ágeis. A ferramenta se adapta ao ritmo de sprints curtos, oferecendo feedback rápido e integrando-se naturalmente aos processos de integração contínua, permitindo entregas mais frequentes e confiáveis.'
  },
  {
    id: 7,
    question: 'Como o Shift-Flare lida com aplicações de página única (SPAs)?',
    answer: 'O Shift-Flare tem algoritmos específicos para lidar com os desafios únicos de SPAs como React, Angular e Vue. Ele entende os ciclos de renderização dessas frameworks e sugere seletores que funcionam mesmo com mudanças dinâmicas no DOM, tornando seus testes mais estáveis.'
  },
  {
    id: 8,
    question: 'Existe suporte técnico disponível para o Shift-Flare?',
    answer: 'Sim, oferecemos suporte técnico dedicado através de chat, e-mail e chamadas de vídeo. Nossos especialistas estão disponíveis para ajudar com implementação, integrações personalizadas e resolução de problemas complexos, garantindo que você aproveite ao máximo nossa ferramenta.'
  },
  {
    id: 9,
    question: 'O Shift-Flare armazena dados sensíveis da minha aplicação?',
    answer: 'Não. O Shift-Flare foi projetado com segurança em mente. Ele processa dados do DOM localmente quando possível e, quando usa nossos servidores, não armazena informações sensíveis. Também oferecemos uma versão on-premises para organizações com requisitos de segurança mais rigorosos.'
  },
  {
    id: 10,
    question: 'Como o Shift-Flare se compara a outras ferramentas de automação de testes?',
    answer: 'Diferente de ferramentas tradicionais que apenas executam testes, o Shift-Flare aplica inteligência artificial para diagnosticar e corrigir falhas automaticamente. Isso reduz drasticamente o tempo de manutenção e as falhas em produção, oferecendo um ROI muito superior às ferramentas convencionais.'
  }
];

// Funcionalidades
export const features: Feature[] = [
  {
    id: 1,
    title: 'Correção Inteligente de XPaths',
    description: 'Algoritmos de IA que analisam a estrutura do DOM e sugerem XPaths mais resilientes quando seus testes falham.',
    icon: 'BrainAI',
    link: '/features/xpath-correction'
  },
  {
    id: 2,
    title: 'Aprendizado Contínuo',
    description: 'O sistema aprende com suas escolhas e melhorias na base de código para fornecer sugestões cada vez mais precisas.',
    icon: 'Repeat',
    link: '/features/continuous-learning'
  },
  {
    id: 3,
    title: 'Integração com CI/CD',
    description: 'API robusta para integrar com suas pipelines de integração contínua, resolvendo problemas automaticamente.',
    icon: 'Cogs',
    link: '/features/ci-integration'
  },
  {
    id: 4,
    title: 'Análise de Resiliência',
    description: 'Identifique pontos fracos em seus testes antes que eles quebrem com nosso sistema de análise preventiva.',
    icon: 'ShieldCheck',
    link: '/features/resilience-analysis'
  }
];

// Itens de navegação
export const navItems: NavItem[] = [
  {
    id: 1,
    label: 'Início',
    href: '#hero',
  },
  {
    id: 2,
    label: 'Funcionalidades',
    href: '#features',
    hasDropdown: true,
    dropdownItems: [
      {
        id: 1,
        label: 'Correção de XPath com IA',
        href: '#xpath-correction',
        description: 'Corrige XPaths quebrados automaticamente'
      },
      {
        id: 2,
        label: 'Automação com Chat Bot',
        href: '#automation',
        description: 'Crie automações a partir de linguagem natural'
      },
      {
        id: 3,
        label: 'Code Review Inteligente',
        href: '#code-review',
        description: 'Análise automática de código'
      },
      {
        id: 4,
        label: 'Migração de Código Q2 → Q3',
        href: '#migration',
        description: 'Converta código legado para o novo padrão'
      }
    ]
  },
  {
    id: 3,
    label: 'Sobre',
    href: '#about',
  },
  {
    id: 5,
    label: 'FAQ',
    href: '#faq',
  }
]; 