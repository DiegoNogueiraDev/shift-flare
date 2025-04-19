"use client";

import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { staggerContainer, fadeIn, slideUp } from '@/utils/animations';
import { faqItems } from '@/utils/data';
import AnimatedFaqItem from '../animations/FaqAnimations';
import Image from 'next/image';

const FAQSection: React.FC = () => {
  const [activeIndex, setActiveIndex] = useState<number | null>(null);
  const [showAllFaqs, setShowAllFaqs] = useState(false);
  
  // Definir o número inicial de FAQs a serem mostradas
  const initialFaqCount = 5;
  
  // Perguntas a serem mostradas com base no estado
  const displayedFaqs = showAllFaqs ? faqItems : faqItems.slice(0, initialFaqCount);

  const toggleAccordion = (index: number) => {
    setActiveIndex(activeIndex === index ? null : index);
  };

  const toggleShowAllFaqs = () => {
    setShowAllFaqs(!showAllFaqs);
    // Resetar o accordion ativo ao expandir/retrair
    setActiveIndex(null);
  };

  return (
    <section id="faq" className="py-20 bg-white relative overflow-hidden">
      {/* Elementos decorativos */}
      <div className="absolute -top-20 -right-20 w-64 h-64 bg-purple-100 rounded-full opacity-30 blur-3xl"></div>
      <div className="absolute -bottom-20 -left-20 w-64 h-64 bg-blue-100 rounded-full opacity-30 blur-3xl"></div>
      
      <div className="container-custom relative z-10">
        {/* Título da seção */}
        <motion.div
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, margin: "-100px" }}
          variants={fadeIn}
          className="text-center mb-16"
        >
          <motion.h2 
            variants={slideUp}
            className="text-3xl md:text-4xl font-bold text-shift-dark mb-4"
          >
            Perguntas Frequentes
          </motion.h2>
          <motion.p 
            variants={slideUp}
            className="text-lg text-gray-600 max-w-2xl mx-auto"
          >
            Encontre respostas para as perguntas mais comuns sobre o Shift-Flare e suas funcionalidades.
          </motion.p>
        </motion.div>

        {/* Acordeões de FAQ */}
        <div className="grid grid-cols-1 lg:grid-cols-5 gap-10">
          {/* Imagem ilustrativa na coluna da esquerda - visível apenas em desktop */}
          <motion.div 
            className="hidden lg:flex lg:col-span-2 items-center justify-center"
            initial={{ opacity: 0, x: -50 }}
            whileInView={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6 }}
            viewport={{ once: true }}
          >
            <div className="relative w-full max-w-sm">
              <Image
                src="/images/faq-illustration.svg"
                alt="FAQ Illustration"
                width={400}
                height={400}
                className="drop-shadow-lg"
              />
            </div>
          </motion.div>
          
          {/* Acordeão na coluna da direita */}
          <motion.div 
            className="lg:col-span-3"
            variants={staggerContainer}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, amount: 0.2 }}
          >
            <div className="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
              {displayedFaqs.map((item, index) => (
                <AnimatedFaqItem
                  key={item.id}
                  item={item}
                  isActive={activeIndex === index}
                  index={index}
                  onToggle={() => toggleAccordion(index)}
                />
              ))}
            </div>
            
            {/* Botão para mostrar mais/menos perguntas */}
            {faqItems.length > initialFaqCount && (
              <motion.div 
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 0.5 }}
                className="mt-8 text-center"
              >
                <button
                  onClick={toggleShowAllFaqs}
                  className="px-6 py-3 bg-gradient-to-r from-shift-purple to-shift-blue text-white rounded-full hover:shadow-lg transition-all duration-300 font-medium group"
                >
                  <span className="flex items-center justify-center">
                    {showAllFaqs ? 'Mostrar menos perguntas' : 'Ver mais perguntas'}
                    <motion.span 
                      className="ml-2"
                      initial={{ rotate: 0 }}
                      animate={{ rotate: showAllFaqs ? 180 : 0 }}
                      transition={{ duration: 0.3 }}
                    >
                      {showAllFaqs ? '↑' : '↓'}
                    </motion.span>
                  </span>
                </button>
              </motion.div>
            )}
          </motion.div>
        </div>
      </div>
    </section>
  );
};

export default FAQSection; 