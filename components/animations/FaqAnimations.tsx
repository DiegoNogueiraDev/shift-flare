"use client";

import React from 'react';
import { motion, Variants } from 'framer-motion';
import { FAQItem } from '@/types';

// Interface para props do componente
export interface FaqItemProps {
  item: FAQItem;
  isActive: boolean;
  index: number;
  onToggle: () => void;
}

// Variantes para as animações
const variants = {
  hidden: { opacity: 0, y: 20 },
  visible: (i: number) => ({
    opacity: 1,
    y: 0,
    transition: {
      delay: i * 0.1,
      duration: 0.5,
      ease: "easeOut"
    }
  }),
  exit: { opacity: 0, y: -20, transition: { duration: 0.3 } },
  hover: {
    backgroundColor: "rgba(124, 58, 237, 0.05)",
    transition: { duration: 0.2 }
  },
  initial: {
    backgroundColor: "transparent",
    transition: { duration: 0.2 }
  }
};

// Variantes para o conteúdo do accordion
const contentVariants: Variants = {
  collapsed: { 
    height: 0, 
    opacity: 0,
    overflow: "hidden",
    transition: { 
      height: { duration: 0.3, ease: "easeInOut" },
      opacity: { duration: 0.2 }
    }
  },
  expanded: { 
    height: "auto", 
    opacity: 1,
    transition: { 
      height: { duration: 0.3, ease: "easeInOut" },
      opacity: { duration: 0.3, delay: 0.1 }
    }
  }
};

// Variantes para o ícone de toggle
const iconVariants: Variants = {
  collapsed: { rotate: 0, transition: { duration: 0.2 } },
  expanded: { rotate: 45, transition: { duration: 0.2 } }
};

const AnimatedFaqItem: React.FC<FaqItemProps> = ({ item, isActive, index, onToggle }) => {
  return (
    <motion.div
      className="border-b border-gray-200 last:border-b-0"
      initial="hidden"
      animate="visible"
      exit="exit"
      custom={index}
      variants={variants}
    >
      <motion.div
        onClick={onToggle}
        className="py-5 px-4 flex justify-between items-center cursor-pointer rounded-lg"
        whileHover="hover"
        initial="initial"
        variants={variants}
      >
        <h3 className="text-lg font-medium text-shift-dark pr-8">{item.question}</h3>
        <motion.div
          variants={iconVariants}
          initial="collapsed"
          animate={isActive ? "expanded" : "collapsed"}
          className="text-shift-purple flex-shrink-0"
        >
          <svg 
            xmlns="http://www.w3.org/2000/svg" 
            width="24" 
            height="24" 
            viewBox="0 0 24 24" 
            fill="none" 
            stroke="currentColor" 
            strokeWidth="2" 
            strokeLinecap="round" 
            strokeLinejoin="round"
          >
            <line x1="12" y1="5" x2="12" y2="19"></line>
            <line x1="5" y1="12" x2="19" y2="12"></line>
          </svg>
        </motion.div>
      </motion.div>

      <motion.div
        variants={contentVariants}
        initial="collapsed"
        animate={isActive ? "expanded" : "collapsed"}
        className="overflow-hidden"
      >
        <div className="px-4 pb-5">
          <p className="text-gray-600">{item.answer}</p>
        </div>
      </motion.div>
    </motion.div>
  );
};

export default AnimatedFaqItem; 