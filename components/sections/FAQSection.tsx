"use client";

import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { staggerContainer } from '@/utils/animations';
import { faqItems } from '@/utils/data';
import AnimatedFaqItem from '../animations/FaqAnimations';

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
    <section id="faq" className="py-20 bg-white">
      <div className="container-custom">
        {/* Título da seção */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          viewport={{ once: true }}
          className="text-center mb-16"
        >
          <h2 className="text-3xl md:text-4xl font-bold text-shift-dark mb-4">
            Perguntas Frequentes
          </h2>
          <p className="text-lg text-gray-600 max-w-2xl mx-auto">
            Encontre respostas para as perguntas mais comuns sobre o Shift-Flare e suas funcionalidades.
          </p>
        </motion.div>

        {/* Acordeões de FAQ */}
        <motion.div 
          className="max-w-3xl mx-auto"
          variants={staggerContainer}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.2 }}
        >
          {displayedFaqs.map((item, index) => (
            <AnimatedFaqItem
              key={item.id}
              item={item}
              isActive={activeIndex === index}
              index={index}
              onToggle={() => toggleAccordion(index)}
            />
          ))}
          
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
                className="px-6 py-2 bg-gradient-to-r from-shift-purple to-shift-blue text-white rounded-md hover:opacity-90 transition-all duration-300 font-medium"
              >
                {showAllFaqs ? 'Mostrar menos perguntas' : 'Ver mais perguntas'}
              </button>
            </motion.div>
          )}
        </motion.div>

        {/* CTA */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.4 }}
          viewport={{ once: true }}
          className="text-center mt-16"
        >
        </motion.div>
      </div>
    </section>
  );
};

export default FAQSection; 