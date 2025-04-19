"use client";

import React from 'react';
import Link from 'next/link';
import { FaFire, FaGithub, FaTwitter, FaLinkedin } from 'react-icons/fa';

const Footer: React.FC = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-shift-dark text-white py-12">
      <div className="container-custom">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Logo e Informações */}
          <div className="col-span-1 md:col-span-1">
            <Link href="/" className="flex items-center space-x-2 text-2xl font-bold mb-4">
              <span>Shift</span>
              <FaFire className="text-shift-red" />
              <span>Flare</span>
            </Link>
            <p className="text-gray-300 mb-4">
              Plataforma de automação inteligente com IA para modernizar seus processos.
            </p>
            <div className="flex space-x-4">
              <a href="https://github.com" target="_blank" rel="noopener noreferrer" className="text-gray-300 hover:text-white transition-colors">
                <FaGithub size={20} />
              </a>
              <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className="text-gray-300 hover:text-white transition-colors">
                <FaTwitter size={20} />
              </a>
              <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" className="text-gray-300 hover:text-white transition-colors">
                <FaLinkedin size={20} />
              </a>
            </div>
          </div>

          {/* Links Rápidos */}
          <div className="col-span-1">
            <h3 className="text-lg font-semibold mb-4">Recursos</h3>
            <ul className="space-y-2">
              <li>
                <Link href="#xpath-correction" className="text-gray-300 hover:text-white transition-colors">
                  Correção de XPath
                </Link>
              </li>
              <li>
                <Link href="#automation" className="text-gray-300 hover:text-white transition-colors">
                  Automação com Chat Bot
                </Link>
              </li>
              <li>
                <Link href="#code-review" className="text-gray-300 hover:text-white transition-colors">
                  Code Review
                </Link>
              </li>
              <li>
                <Link href="#migration" className="text-gray-300 hover:text-white transition-colors">
                  Migração de Código
                </Link>
              </li>
            </ul>
          </div>

          {/* Projeto */}
          <div className="col-span-1">
            <h3 className="text-lg font-semibold mb-4">Projeto</h3>
            <ul className="space-y-2">
              <li>
                <Link href="#about" className="text-gray-300 hover:text-white transition-colors">
                  Sobre nós
                </Link>
              </li>
              <li>
                <Link href="#team" className="text-gray-300 hover:text-white transition-colors">
                  Equipe
                </Link>
              </li>
              <li>
                <Link href="#faq" className="text-gray-300 hover:text-white transition-colors">
                  FAQ
                </Link>
              </li>
              <li>
                <a href="/swagger-ui.html" target="_blank" className="text-gray-300 hover:text-white transition-colors">
                  API Docs
                </a>
              </li>
            </ul>
          </div>

          {/* Contato */}
          <div className="col-span-1">
            <h3 className="text-lg font-semibold mb-4">Contato</h3>
            <p className="text-gray-300 mb-2">contato@shiftflare.com.br</p>
            <p className="text-gray-300 mb-4">+55 (11) 1234-5678</p>
            <p className="text-gray-300">
              São Paulo, SP<br />
              Brasil
            </p>
          </div>
        </div>

        {/* Linha Divisória */}
        <div className="border-t border-gray-800 mt-10 pt-6">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <p className="text-gray-400 text-sm">
              &copy; {currentYear} Shift-Flare. Todos os direitos reservados.
            </p>
            <div className="flex space-x-6 mt-4 md:mt-0">
              <Link href="/privacy" className="text-gray-400 hover:text-white text-sm transition-colors">
                Privacidade
              </Link>
              <Link href="/terms" className="text-gray-400 hover:text-white text-sm transition-colors">
                Termos de Uso
              </Link>
              <Link href="/cookies" className="text-gray-400 hover:text-white text-sm transition-colors">
                Cookies
              </Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 