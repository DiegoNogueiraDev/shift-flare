"use client";

import React from 'react';
import { motion } from 'framer-motion';
import Button from '@/components/ui/Button';
import { fadeIn, slideUp } from '@/utils/animations';
import { FaFire, FaArrowRight, FaPlay } from 'react-icons/fa';

const HeroSection: React.FC = () => {
  return (
    <section 
      id="hero" 
      className="pt-28 pb-20 bg-gradient-to-br from-shift-purple to-shift-blue text-white overflow-hidden relative"
    >
      <div className="container-custom relative z-10">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          {/* Conteúdo */}
          <motion.div
            initial="hidden"
            animate="visible"
            variants={fadeIn}
            className="space-y-6"
          >
            {/* Badge animada */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
              className="inline-flex items-center bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm"
            >
              <FaFire className="text-shift-red mr-2" />
              <span>Potencialize sua automação com IA</span>
            </motion.div>

            {/* Título */}
            <motion.h1
              variants={slideUp}
              className="text-4xl md:text-5xl lg:text-6xl font-bold leading-tight"
            >
              Automação Inteligente para o seu Código
            </motion.h1>

            {/* Descrição */}
            <motion.p 
              variants={slideUp}
              className="text-lg md:text-xl text-white/80 max-w-xl"
            >
              Corrija XPaths, analise código, gere scripts de automação e migre código legado, tudo com a ajuda da inteligência artificial.
            </motion.p>

            {/* Botões de ação */}
            <motion.div 
              variants={slideUp}
              className="flex flex-wrap gap-4 pt-4"
            >
              <Button 
                variant="primary" 
                size="lg"
                className="bg-white text-shift-purple hover:bg-gray-100"
                icon={<FaArrowRight />}
                iconPosition="right"
              >
                Começar agora
              </Button>
              <Button 
                variant="outline" 
                size="lg"
                className="border-white text-white hover:bg-white/10"
                icon={<FaPlay />}
                iconPosition="left"
              >
                Ver Documentação
              </Button>
            </motion.div>

            {/* Estatísticas */}
            <motion.div
              variants={slideUp}
              className="grid grid-cols-3 gap-4 pt-8 border-t border-white/20 mt-8"
            >
              <div className="bg-white/50 p-3 rounded-lg backdrop-blur-sm">
                <div className="text-3xl font-bold text-shift-orange">98%</div>
                <div className="text-shift-orange font-medium text-sm">Precisão</div>
              </div>
              <div className="bg-white/50 p-3 rounded-lg backdrop-blur-sm">
                <div className="text-3xl font-bold text-shift-orange">4+</div>
                <div className="text-shift-orange font-medium text-sm">Funcionalidades</div>
              </div>
              <div className="bg-white/50 p-3 rounded-lg backdrop-blur-sm">
                <div className="text-3xl font-bold text-shift-orange">24/7</div>
                <div className="text-shift-orange font-medium text-sm">Disponibilidade</div>
              </div>
            </motion.div>
          </motion.div>

          {/* Imagem/Ilustração */}
          <motion.div
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.8, ease: "easeOut" }}
            className="relative"
          >
            <div className="relative z-10 backdrop-blur-sm bg-white/10 p-6 rounded-2xl shadow-xl border border-white/20">
              <pre className="text-sm md:text-base text-white/90 overflow-x-auto">
                <code>{`
// Exemplo de correção de XPath com IA
const brokenXPath = "//div[@id='old-id']";
const pageDOM = "<div id='new-id'>Conteúdo</div>";

// Shift-Flare em ação
const newXPath = await shiftFlare.correctXPath({
  errorXpath: brokenXPath,
  pageDOM: pageDOM
});

console.log(newXPath); 
// → "//div[@id='new-id']"
                `}</code>
              </pre>
            </div>

            {/* Elementos decorativos */}
            <div className="absolute -top-10 -right-10 w-40 h-40 bg-shift-red rounded-full opacity-20 blur-3xl"></div>
            <div className="absolute -bottom-10 -left-10 w-40 h-40 bg-shift-orange rounded-full opacity-20 blur-3xl"></div>
          </motion.div>
        </div>
      </div>

      {/* Ondas decorativas - ajustando posição */}
      <div className="absolute bottom-0 left-0 right-0 w-full overflow-hidden leading-0 transform translate-y-1">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320" className="w-full">
          <path fill="#f9fafb" fillOpacity="1" d="M0,288L48,272C96,256,192,224,288,213.3C384,203,480,213,576,229.3C672,245,768,267,864,261.3C960,256,1056,224,1152,197.3C1248,171,1344,149,1392,138.7L1440,128L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path>
        </svg>
      </div>
    </section>
  );
};

export default HeroSection; 