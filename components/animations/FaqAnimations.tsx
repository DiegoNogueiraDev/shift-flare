"use client";

import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FAQItem } from '@/types';

interface FaqItemProps {
  item: FAQItem;
  isActive: boolean;
  index: number;
  onToggle: () => void;
}

const variants = {
  hidden: {
    opacity: 0,
    y: 10,
    scale: 0.95
  },
  visible: (index: number) => ({
    opacity: 1,
    y: 0,
    scale: 1,
    transition: {
      delay: index * 0.1,
      duration: 0.5,
      ease: 'easeOut'
    }
  }),
  exit: {
    opacity: 0,
    height: 0,
    transition: {
      duration: 0.3,
      ease: 'easeInOut'
    }
  }
};

const headerVariants = {
  hover: { 
    scale: 1.01,
    transition: { duration: 0.2 } 
  },
  active: {
    backgroundColor: 'var(--shift-purple)',
    color: '#fff',
    transition: { duration: 0.2 }
  },
  inactive: {
    backgroundColor: '#f9fafb',
    color: '#1f2937',
    transition: { duration: 0.2 }
  }
};

const contentVariants = {
  collapsed: { 
    height: 0, 
    opacity: 0, 
    transition: { 
      duration: 0.3,
      ease: 'easeInOut'
    } 
  },
  expanded: { 
    height: 'auto', 
    opacity: 1, 
    transition: { 
      duration: 0.3,
      ease: 'easeOut' 
    } 
  }
};

const iconVariants = {
  collapsed: { 
    rotate: 0, 
    backgroundColor: 'rgba(124, 58, 237, 0.1)',
    color: 'rgb(124, 58, 237)',
    transition: { duration: 0.2 } 
  },
  expanded: { 
    rotate: 45, 
    backgroundColor: '#fff',
    color: 'rgb(124, 58, 237)',
    transition: { duration: 0.2 } 
  }
};

const AnimatedFaqItem: React.FC<FaqItemProps> = ({ 
  item, 
  isActive, 
  index, 
  onToggle 
}) => {
  return (
    <motion.div
      className="mb-4 overflow-hidden"
      custom={index}
      initial="hidden"
      animate="visible"
      variants={variants}
      layoutId={`faq-item-${item.id}`}
    >
      <motion.div
        className="rounded-lg overflow-hidden shadow-sm"
        animate={isActive ? "active" : "inactive"}
        whileHover="hover"
        variants={headerVariants}
      >
        <motion.div
          className={`flex justify-between items-center p-5 cursor-pointer`}
          onClick={onToggle}
        >
          <h3 className="text-lg font-medium">{item.question}</h3>
          <motion.div
            className="h-6 w-6 flex items-center justify-center rounded-full"
            animate={isActive ? "expanded" : "collapsed"}
            variants={iconVariants}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M12 6v6m0 0v6m0-6h6m-6 0H6"
              />
            </svg>
          </motion.div>
        </motion.div>
      </motion.div>

      <AnimatePresence>
        {isActive && (
          <motion.div
            initial="collapsed"
            animate="expanded"
            exit="collapsed"
            variants={contentVariants}
            className="bg-white px-5 overflow-hidden border-l border-r border-b rounded-b-lg"
          >
            <div className="py-4">
              <p className="text-gray-600">{item.answer}</p>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default AnimatedFaqItem; 