"use client";

import React from 'react';
import { motion } from 'framer-motion';
import { staggerContainer, cardAnimation } from '@/utils/animations';
import { features } from '@/utils/data';
import Card from '@/components/ui/Card';
import { FaCode, FaRobot, FaMagic, FaExchangeAlt } from 'react-icons/fa';

// Mapeamento de ícones para as funcionalidades
const iconMap: Record<string, JSX.Element> = {
  FaCode: <FaCode size={24} className="text-shift-purple" />,
  FaRobot: <FaRobot size={24} className="text-shift-blue" />,
  FaMagic: <FaMagic size={24} className="text-shift-red" />,
  FaExchangeAlt: <FaExchangeAlt size={24} className="text-shift-orange" />,
};

const FeaturesSection: React.FC = () => {
  return (
    <section id="features" className="py-20 bg-gray-50 relative">
      <div className="container-custom">
        {/* Título da seção */}
        <div className="text-center mb-16">
          <motion.h2
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            viewport={{ once: true }}
            className="text-3xl md:text-4xl font-bold text-shift-dark mb-4"
          >
            Funcionalidades Poderosas
          </motion.h2>
          <motion.p
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.1 }}
            viewport={{ once: true }}
            className="text-lg text-gray-600 max-w-2xl mx-auto"
          >
            Automatize e modernize seus processos de desenvolvimento com nossas soluções baseadas em inteligência artificial.
          </motion.p>
        </div>

        {/* Grid de funcionalidades */}
        <motion.div
          variants={staggerContainer}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, amount: 0.2 }}
          className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8"
        >
          {features.map((feature) => (
            <motion.div
              key={feature.id}
              variants={cardAnimation}
              whileHover={{ y: -10 }}
              transition={{ duration: 0.3 }}
            >
              <Card className="h-full flex flex-col">
                <div className="p-4 bg-gray-100 rounded-lg inline-block mb-4">
                  {iconMap[feature.icon]}
                </div>
                <h3 className="text-xl font-semibold text-shift-dark mb-3">
                  {feature.title}
                </h3>
                <p className="text-gray-600 flex-grow mb-4">
                  {feature.description}
                </p>
                <a 
                  href={feature.link} 
                  className="text-shift-purple font-medium flex items-center hover:underline"
                >
                  Saiba mais
                  <svg 
                    className="ml-1 w-4 h-4" 
                    fill="none" 
                    stroke="currentColor" 
                    viewBox="0 0 24 24" 
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path 
                      strokeLinecap="round" 
                      strokeLinejoin="round" 
                      strokeWidth={2} 
                      d="M9 5l7 7-7 7" 
                    />
                  </svg>
                </a>
              </Card>
            </motion.div>
          ))}
        </motion.div>

        {/* CTA */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.3 }}
          viewport={{ once: true }}
          className="mt-16 text-center"
        >
          <a
            href="/swagger-ui.html"
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-gradient-to-r from-shift-purple to-shift-blue hover:opacity-90 focus:outline-none"
          >
            Explorar a API
            <svg
              className="ml-2 -mr-1 h-5 w-5"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                fillRule="evenodd"
                d="M10.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L12.586 11H5a1 1 0 110-2h7.586l-2.293-2.293a1 1 0 010-1.414z"
                clipRule="evenodd"
              />
            </svg>
          </a>
        </motion.div>
      </div>
    </section>
  );
};

export default FeaturesSection; 