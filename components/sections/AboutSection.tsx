"use client";

import React from 'react';
import { motion } from 'framer-motion';
import Card from '@/components/ui/Card';
import { fadeIn, slideUp } from '@/utils/animations';

const AboutSection: React.FC = () => {
  return (
    <section id="about" className="py-20 bg-white">
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
            Sobre o Shift-Flare
          </h2>
          <p className="text-lg text-gray-600 max-w-2xl mx-auto">
            Conheça nossa missão de transformar o desenvolvimento de testes automatizados com inteligência artificial.
          </p>
        </motion.div>

        {/* Grid de conteúdo */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-12 items-center">
          {/* Imagem */}
          <motion.div
            initial={{ opacity: 0, x: -30 }}
            whileInView={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5 }}
            viewport={{ once: true }}
          >
            <div className="rounded-lg overflow-hidden shadow-lg">
              <img 
                src="/about/team-working.jpg" 
                alt="Equipe Shift-Flare trabalhando"
                className="w-full h-auto" 
              />
            </div>
          </motion.div>

          {/* Texto */}
          <motion.div
            initial={{ opacity: 0, x: 30 }}
            whileInView={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
            viewport={{ once: true }}
            className="space-y-6"
          >
            <h3 className="text-2xl font-bold text-shift-dark">Nossa Missão</h3>
            <p className="text-gray-600">
              O Shift-Flare nasceu da necessidade de resolver um dos maiores desafios na automação de testes: a fragilidade dos seletores e XPaths. Nossa equipe de especialistas passou anos lidando com testes quebrados devido a pequenas mudanças no DOM.
            </p>
            <p className="text-gray-600">
              Combinando experiência em automação com tecnologias avançadas de IA, criamos uma plataforma que não apenas corrige problemas, mas evolui e aprende continuamente para se adaptar ao seu código.
            </p>
            
            {/* Cartões de valores */}
            <div className="grid grid-cols-2 gap-4 pt-4">
              <Card className="p-4 text-center">
                <div className="text-shift-purple text-lg font-bold mb-2">Inovação</div>
                <p className="text-sm text-gray-600">Constantemente evoluindo nossas técnicas de IA</p>
              </Card>
              <Card className="p-4 text-center">
                <div className="text-shift-blue text-lg font-bold mb-2">Qualidade</div>
                <p className="text-sm text-gray-600">Comprometidos com resultados precisos e confiáveis</p>
              </Card>
            </div>
          </motion.div>
        </div>

        {/* Métricas */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.3 }}
          viewport={{ once: true }}
          className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-20"
        >
          <div className="bg-gradient-to-br from-shift-purple to-shift-blue text-white rounded-lg p-8 text-center">
            <div className="text-4xl font-bold mb-2">+10.000</div>
            <div className="text-lg">XPaths corrigidos</div>
          </div>
          <div className="bg-gradient-to-br from-shift-red to-shift-orange text-white rounded-lg p-8 text-center">
            <div className="text-4xl font-bold mb-2">98%</div>
            <div className="text-lg">Taxa de precisão</div>
          </div>
          <div className="bg-gradient-to-br from-shift-blue to-shift-purple text-white rounded-lg p-8 text-center">
            <div className="text-4xl font-bold mb-2">+500</div>
            <div className="text-lg">Empresas satisfeitas</div>
          </div>
        </motion.div>

        {/* Destaque para o criador */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.4 }}
          viewport={{ once: true }}
          className="mt-16 text-center"
        >
          <div className="relative backdrop-blur-sm bg-gradient-to-r from-shift-purple/10 to-shift-blue/10 p-6 rounded-xl border border-white/20 shadow-xl max-w-3xl mx-auto overflow-hidden">
            {/* Elementos decorativos de fundo */}
            <div className="absolute -top-10 -left-10 w-40 h-40 bg-shift-orange/20 rounded-full blur-3xl"></div>
            <div className="absolute -bottom-10 -right-10 w-40 h-40 bg-shift-red/20 rounded-full blur-3xl"></div>
            
            <div className="relative z-10 flex flex-col md:flex-row items-center justify-center gap-4">
              <div className="flex-shrink-0 bg-gradient-to-br from-shift-orange to-shift-red p-3 rounded-full">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-10 w-10 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
              </div>
              <div className="text-center md:text-left">
                <h4 className="text-xl font-bold text-shift-dark mb-1">Hercules IA</h4>
                <p className="text-shift-purple font-medium">
                  Criado pela Equipe De Automação B2B
                </p>
              </div>
            </div>
          </div>
        </motion.div>
      </div>
    </section>
  );
};

export default AboutSection; 