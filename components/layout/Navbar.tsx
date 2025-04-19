"use client";

import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import Link from 'next/link';
import { navbarAnimation, slideDown } from '@/utils/animations';
import { navItems } from '@/utils/data';
import { FaFire, FaChevronDown } from 'react-icons/fa';
import Button from '@/components/ui/Button';

const Navbar: React.FC = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [activeDropdown, setActiveDropdown] = useState<number | null>(null);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  // Monitora o scroll para mudar a aparência da navbar
  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 10) {
        setIsScrolled(true);
      } else {
        setIsScrolled(false);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleDropdown = (id: number) => {
    if (activeDropdown === id) {
      setActiveDropdown(null);
    } else {
      setActiveDropdown(id);
    }
  };

  const closeDropdown = () => {
    setActiveDropdown(null);
  };

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  return (
    <motion.nav
      className={`fixed top-0 left-0 right-0 z-50 ${
        isScrolled 
          ? 'bg-navbar-scrolled shadow-nav py-3 text-shift-dark backdrop-blur-sm' 
          : 'bg-transparent py-5 text-white'
      } transition-all duration-300`}
      initial="hidden"
      animate="visible"
      variants={navbarAnimation}
    >
      <div className="container-custom mx-auto px-4 flex items-center justify-between">
        {/* Logo */}
        <Link href="/" className={`flex items-center space-x-2 text-2xl font-bold ${
          isScrolled ? 'text-shift-purple' : 'text-white text-shadow-sm'
        }`}>
          <span>Shift</span>
          <FaFire className={isScrolled ? "text-shift-red" : "text-shift-red-light"} />
          <span>Flare</span>
        </Link>

        {/* Desktop Navigation */}
        <div className="hidden md:flex items-center space-x-6">
          {navItems.map((item) => (
            <div key={item.id} className="relative group">
              {item.hasDropdown ? (
                <button 
                  className={`flex items-center space-x-1 font-medium ${
                    isScrolled 
                      ? 'text-gray-700 hover:text-shift-purple' 
                      : 'text-white hover:text-white text-shadow-sm'
                  }`}
                  onClick={() => toggleDropdown(item.id)}
                >
                  <span>{item.label}</span>
                  <FaChevronDown className={`h-3 w-3 transition-transform ${
                    activeDropdown === item.id ? 'transform rotate-180' : ''
                  }`} />
                </button>
              ) : (
                <Link 
                  href={item.href} 
                  className={`font-medium ${
                    isScrolled 
                      ? 'text-gray-700 hover:text-shift-purple' 
                      : 'text-white hover:text-white text-shadow-sm'
                  }`}
                  onClick={closeDropdown}
                >
                  {item.label}
                </Link>
              )}

              {/* Dropdown Menu */}
              {item.hasDropdown && (
                <AnimatePresence>
                  {activeDropdown === item.id && (
                    <motion.div
                      initial="hidden"
                      animate="visible"
                      exit="hidden"
                      variants={slideDown}
                      className="absolute top-full left-0 mt-2 w-64 bg-white rounded-lg shadow-lg overflow-hidden py-2"
                    >
                      {item.dropdownItems?.map((dropdownItem) => (
                        <Link
                          key={dropdownItem.id}
                          href={dropdownItem.href}
                          className="block px-4 py-2 hover:bg-gray-50"
                          onClick={() => {
                            closeDropdown();
                            setIsMobileMenuOpen(false);
                          }}
                        >
                          <div className="font-medium text-gray-800">{dropdownItem.label}</div>
                          {dropdownItem.description && (
                            <div className="text-sm text-gray-500">{dropdownItem.description}</div>
                          )}
                        </Link>
                      ))}
                    </motion.div>
                  )}
                </AnimatePresence>
              )}
            </div>
          ))}
        </div>

        {/* CTA Button */}
        <div className="hidden md:block">
          <Button 
            variant={isScrolled ? "primary" : "outline"}
            size="md"
            className={isScrolled ? "" : "border-white text-white hover:bg-white/20 font-medium"}
          >
            Começar agora
          </Button>
        </div>

        {/* Mobile Menu Button */}
        <button
          className={`md:hidden ${
            isScrolled 
              ? 'text-gray-700 hover:text-shift-purple' 
              : 'text-white hover:text-white/80'
          }`}
          onClick={toggleMobileMenu}
        >
          <svg
            className="w-6 h-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            {isMobileMenuOpen ? (
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            ) : (
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              />
            )}
          </svg>
        </button>
      </div>

      {/* Mobile Menu */}
      <AnimatePresence>
        {isMobileMenuOpen && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            className="md:hidden bg-white border-t"
          >
            <div className="container-custom mx-auto px-4 py-4 space-y-4">
              {navItems.map((item) => (
                <div key={item.id} className="py-2">
                  {item.hasDropdown ? (
                    <div>
                      <button 
                        className="flex items-center justify-between w-full text-left text-gray-700 hover:text-shift-purple"
                        onClick={() => toggleDropdown(item.id)}
                      >
                        <span>{item.label}</span>
                        <FaChevronDown className={`h-3 w-3 transition-transform ${
                          activeDropdown === item.id ? 'transform rotate-180' : ''
                        }`} />
                      </button>
                      
                      <AnimatePresence>
                        {activeDropdown === item.id && (
                          <motion.div
                            initial={{ opacity: 0, height: 0 }}
                            animate={{ opacity: 1, height: 'auto' }}
                            exit={{ opacity: 0, height: 0 }}
                            className="mt-2 ml-4 space-y-2"
                          >
                            {item.dropdownItems?.map((dropdownItem) => (
                              <Link
                                key={dropdownItem.id}
                                href={dropdownItem.href}
                                className="block py-2 text-gray-600 hover:text-shift-purple"
                                onClick={() => {
                                  closeDropdown();
                                  setIsMobileMenuOpen(false);
                                }}
                              >
                                {dropdownItem.label}
                              </Link>
                            ))}
                          </motion.div>
                        )}
                      </AnimatePresence>
                    </div>
                  ) : (
                    <Link 
                      href={item.href} 
                      className="block text-gray-700 hover:text-shift-purple"
                      onClick={() => setIsMobileMenuOpen(false)}
                    >
                      {item.label}
                    </Link>
                  )}
                </div>
              ))}
              
              <div className="pt-2">
                <Button 
                  variant="primary"
                  size="md"
                  fullWidth
                >
                  Começar agora
                </Button>
              </div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </motion.nav>
  );
};

export default Navbar; 