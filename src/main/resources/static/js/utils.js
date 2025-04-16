/**
 * Utilities - Funções utilitárias para o site
 */

/**
 * Anima elementos quando entram na viewport
 */
const animateOnScroll = () => {
  const elements = document.querySelectorAll('.animate-on-scroll');
  
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('animated');
        observer.unobserve(entry.target);
      }
    });
  }, {
    threshold: 0.1
  });
  
  elements.forEach(element => {
    observer.observe(element);
  });
};

/**
 * Atualiza o contador de estatísticas
 */
const startCounters = () => {
  const counters = document.querySelectorAll('.counter');
  
  counters.forEach(counter => {
    const target = parseInt(counter.getAttribute('data-target'));
    const duration = 2000; // 2 segundos
    const step = target / (duration / 16); // 60fps
    
    let current = 0;
    const updateCounter = () => {
      current += step;
      if (current < target) {
        counter.textContent = Math.floor(current);
        requestAnimationFrame(updateCounter);
      } else {
        counter.textContent = target;
      }
    };
    
    updateCounter();
  });
};

/**
 * Inicializa tooltips Bootstrap
 */
const initTooltips = () => {
  const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
  tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
  });
};

/**
 * Gerencia navegação para links de âncora internos
 * Verifica se estamos na página inicial antes de navegar para âncoras
 */
const setupInternalNavigation = () => {
  // Função para lidar com cliques em links internos
  const handleInternalLink = function(e) {
    const targetId = this.getAttribute('href');
    const currentPath = window.location.pathname;
    
    // Se não estamos na página inicial mas o link é para uma âncora interna
    if (!currentPath.endsWith('index.html') && 
        !currentPath.endsWith('/') && 
        currentPath !== '' && 
        currentPath !== '/' && 
        targetId.startsWith('#')) {
      
      e.preventDefault();
      console.log('Link interno clicado em página externa, redirecionando');
      
      // Determina o caminho relativo correto para a página inicial
      let indexPath = 'index.html';
      
      // Se o caminho atual contém diretórios como 'src/main/resources/static/'
      if (currentPath.includes('/static/')) {
        // Extrai o caminho base até o diretório static
        const basePathMatch = currentPath.match(/(.*\/static\/)/);
        if (basePathMatch && basePathMatch[1]) {
          const basePath = basePathMatch[1];
          indexPath = basePath + 'index.html';
        }
      }
      
      // Redirecionar para a página inicial com a âncora
      window.location.href = indexPath + targetId;
    }
  };
  
  // Seleciona todos os links que têm a classe internal-link
  document.querySelectorAll('.internal-link').forEach(link => {
    link.addEventListener('click', handleInternalLink);
  });
  
  // Também capturar links de âncora diretamente na página (fora dos componentes carregados dinamicamente)
  document.querySelectorAll('a[href^="#"]').forEach(link => {
    // Verifica se não é um link de componente que já tem a classe internal-link
    if (!link.classList.contains('internal-link')) {
      // Verifica se é um dos links para seções da página principal
      const href = link.getAttribute('href');
      if (['#ai-features', '#faq', '#creator'].includes(href)) {
        link.addEventListener('click', handleInternalLink);
      }
    }
  });
};

/**
 * Verifica se há uma âncora na URL e rola para o elemento correspondente
 * Também redireciona para a página certa se a âncora pertencer a outra página
 */
const scrollToAnchorOnLoad = () => {
  // Obtém a âncora da URL atual
  const hash = window.location.hash;
  
  if (hash) {
    console.log('Hash detectado:', hash);
    
    // Lista de âncoras que pertencem à página inicial
    const mainPageAnchors = ['#ai-features', '#faq', '#creator'];
    const currentPath = window.location.pathname;
    
    console.log('Caminho atual:', currentPath);
    
    // Verifica se estamos na página principal
    const isMainPage = currentPath.endsWith('index.html') || 
                      currentPath.endsWith('/') || 
                      currentPath === '' ||
                      currentPath === '/';
    
    console.log('É página principal?', isMainPage);
    console.log('A âncora pertence à página principal?', mainPageAnchors.includes(hash));
    
    // Se não estamos na página principal, mas a âncora pertence à página principal
    if (!isMainPage && mainPageAnchors.includes(hash)) {
      // Determina o caminho relativo correto para a página inicial
      let indexPath = 'index.html';
      
      // Se o caminho atual contém diretórios como 'src/main/resources/static/'
      if (currentPath.includes('/static/')) {
        // Extrai o caminho base até o diretório static
        const basePathMatch = currentPath.match(/(.*\/static\/)/);
        if (basePathMatch && basePathMatch[1]) {
          const basePath = basePathMatch[1];
          indexPath = basePath + 'index.html';
        }
      }
      
      console.log('Redirecionando para:', indexPath + hash);
      
      // Redirecionar para a página inicial com a âncora
      window.location.href = indexPath + hash;
      return; // Interrompe a execução, pois vamos carregar uma nova página
    }
    
    // Se a âncora é válida para a página atual, rolar até ela após um pequeno atraso
    setTimeout(() => {
      const targetElement = document.querySelector(hash);
      
      if (targetElement) {
        console.log('Elemento encontrado, rolando para:', hash);
        // Rola suavemente até o elemento
        targetElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
      } else {
        console.log('Elemento não encontrado na página:', hash);
      }
    }, 300);
  }
};

// Inicializa os utilitários quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
  // Inicializa os tooltips
  initTooltips();
  
  // Inicia as animações ao scroll
  animateOnScroll();
  
  // Configurar navegação interna
  setupInternalNavigation();
  
  // Adicionar comportamento especial para links na página inicial
  setupSmoothScrollOnHomepage();
  
  // Verificar e rolar para âncora se houver
  scrollToAnchorOnLoad();
  
  // Inicia os contadores quando a seção de estatísticas estiver visível
  const statsSection = document.querySelector('#statistics');
  if (statsSection) {
    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        startCounters();
        observer.unobserve(entries[0].target);
      }
    }, { threshold: 0.5 });
    
    observer.observe(statsSection);
  }
});

/**
 * Configura rolagem suave para links de âncora quando estiver na página inicial
 */
const setupSmoothScrollOnHomepage = () => {
  // Verifica se estamos na página inicial
  const currentPath = window.location.pathname;
  const isHomePage = currentPath.endsWith('index.html') || 
                      currentPath.endsWith('/') || 
                      currentPath === '' ||
                      currentPath === '/';
  
  if (isHomePage) {
    // Seleciona links que apontam para âncoras dentro da própria página inicial
    const indexLinks = document.querySelectorAll('a[href^="index.html#"]');
    
    indexLinks.forEach(link => {
      link.addEventListener('click', function(e) {
        e.preventDefault();
        
        // Extrair apenas a parte da âncora
        const href = this.getAttribute('href');
        const anchorPart = href.substring(href.indexOf('#'));
        
        // Obter o elemento alvo
        const targetElement = document.querySelector(anchorPart);
        
        if (targetElement) {
          // Rolar suavemente até o elemento
          targetElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
          
          // Atualizar a URL sem recarregar a página
          window.history.pushState(null, '', anchorPart);
        }
      });
    });
  }
}; 