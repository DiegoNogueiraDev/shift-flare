import { Variants } from "framer-motion";

// Animação de fade-in
export const fadeIn: Variants = {
  hidden: { opacity: 0 },
  visible: { 
    opacity: 1,
    transition: { duration: 0.5, ease: "easeOut" }
  }
};

// Animação de slide-up (para cards, seções, etc)
export const slideUp: Variants = {
  hidden: { opacity: 0, y: 30 },
  visible: { 
    opacity: 1, 
    y: 0,
    transition: { duration: 0.5, ease: "easeOut" }
  }
};

// Animação de slide-down (para dropdown, etc)
export const slideDown: Variants = {
  hidden: { opacity: 0, y: -10, height: 0 },
  visible: { 
    opacity: 1, 
    y: 0,
    height: "auto",
    transition: { duration: 0.3, ease: "easeOut" }
  }
};

// Animação para cartões em sequência (staggered)
export const staggerContainer: Variants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.1
    }
  }
};

// Animação para cartão individual
export const cardAnimation: Variants = {
  hidden: { opacity: 0, y: 20 },
  visible: { 
    opacity: 1, 
    y: 0,
    transition: {
      duration: 0.4,
      ease: "easeOut"
    }
  },
  hover: {
    y: -10,
    boxShadow: "0 10px 20px rgba(0, 0, 0, 0.1)",
    transition: {
      duration: 0.2,
      ease: "easeOut"
    }
  }
};

// Animação para o botão
export const buttonAnimation: Variants = {
  hover: {
    scale: 1.05,
    transition: {
      duration: 0.2,
      ease: "easeInOut"
    }
  },
  tap: {
    scale: 0.95,
    transition: {
      duration: 0.1,
      ease: "easeInOut"
    }
  }
};

// Animação para o acordeão FAQ
export const accordionAnimation: Variants = {
  hidden: { height: 0, opacity: 0, overflow: "hidden" },
  visible: { 
    height: "auto", 
    opacity: 1,
    overflow: "visible",
    transition: { 
      height: {
        duration: 0.3,
        ease: "easeOut"
      },
      opacity: {
        duration: 0.2,
        ease: "easeOut",
        delay: 0.1
      }
    }
  }
};

// Animação para o ícone do acordeão
export const plusIconAnimation: Variants = {
  closed: { rotate: 0 },
  open: { rotate: 45, transition: { duration: 0.2 } }
};

// Animações para a navbar
export const navbarAnimation: Variants = {
  hidden: { opacity: 0, y: -20 },
  visible: { 
    opacity: 1, 
    y: 0,
    transition: {
      duration: 0.5,
      ease: "easeOut"
    }
  }
};

// Efeito de gradiente em movimento
export const gradientAnimation: Variants = {
  animate: {
    backgroundPosition: ["0% 50%", "100% 50%", "0% 50%"],
    transition: {
      duration: 10,
      ease: "easeInOut",
      repeat: Infinity,
      repeatType: "reverse"
    }
  }
};

// Animação para itens do FAQ
export const faqItemAnimation: Variants = {
  hidden: { 
    opacity: 0, 
    x: -20,
    filter: "blur(5px)" 
  },
  visible: { 
    opacity: 1, 
    x: 0,
    filter: "blur(0px)",
    transition: { 
      duration: 0.5, 
      ease: "easeOut" 
    }
  },
  hover: {
    scale: 1.02,
    backgroundColor: "rgba(124, 58, 237, 0.05)",
    transition: {
      duration: 0.2,
      ease: "easeInOut"
    }
  }
}; 