/**
 * Component Loader - Carrega componentes HTML dinamicamente
 */
document.addEventListener('DOMContentLoaded', () => {
  // Elementos que precisam ser carregados com componentes
  const componentContainers = document.querySelectorAll('[data-component]');
  
  // Função para carregar um componente HTML
  const loadComponent = async (element) => {
    const componentName = element.getAttribute('data-component');
    
    try {
      const response = await fetch(`components/${componentName}.html`);
      
      if (!response.ok) {
        throw new Error(`Erro ao carregar o componente ${componentName}: ${response.status}`);
      }
      
      const html = await response.text();
      element.innerHTML = html;
      
      // Verificar se precisa ativar o componente após carregá-lo
      if (element.hasAttribute('data-activate')) {
        activateComponent(componentName, element);
      }
      
    } catch (error) {
      console.error(`Falha ao carregar o componente ${componentName}:`, error);
      element.innerHTML = `<div class="alert alert-danger">Erro ao carregar o componente: ${componentName}</div>`;
    }
  };
  
  // Função para ativar funcionalidades específicas de componentes
  const activateComponent = (componentName, element) => {
    switch(componentName) {
      case 'navbar':
        // Ativar a navegação responsiva ou outras funcionalidades
        highlightCurrentNavItem();
        break;
      
      case 'features':
        // Alguma funcionalidade específica para features
        break;
      
      case 'faq':
        // Ativar o primeiro item do FAQ, se necessário
        break;
      
      default:
        // Nenhuma ação adicional necessária
        break;
    }
  };
  
  // Função para destacar o item de navegação atual
  const highlightCurrentNavItem = () => {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
      const href = link.getAttribute('href');
      
      // Destacar o link correspondente à página atual
      if ((currentPath.endsWith(href) || (currentPath === '/' && href === 'index.html'))) {
        link.classList.add('active');
      }
    });
  };
  
  // Carregar todos os componentes
  componentContainers.forEach(container => {
    loadComponent(container);
  });
}); 