"use client";

import React from 'react';
import { motion } from 'framer-motion';
import { buttonAnimation } from '@/utils/animations';
import classNames from 'classnames';

interface ButtonProps {
  children: React.ReactNode;
  variant?: 'primary' | 'secondary' | 'outline' | 'text';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  onClick?: () => void;
  disabled?: boolean;
  type?: 'button' | 'submit' | 'reset';
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  fullWidth?: boolean;
}

const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  className = '',
  onClick,
  disabled = false,
  type = 'button',
  icon,
  iconPosition = 'left',
  fullWidth = false,
}) => {
  const baseClasses = 'inline-flex items-center justify-center rounded-md font-medium transition-all focus:outline-none';
  
  const variantClasses = {
    primary: 'bg-gradient-to-r from-shift-purple to-shift-blue text-white shadow-md hover:opacity-90',
    secondary: 'bg-white text-shift-purple border border-shift-purple hover:bg-gray-50',
    outline: 'bg-transparent border border-shift-purple text-shift-purple hover:bg-shift-purple/5',
    text: 'bg-transparent text-shift-purple hover:bg-shift-purple/5',
  };
  
  const sizeClasses = {
    sm: 'text-sm px-3 py-1.5',
    md: 'text-base px-5 py-2',
    lg: 'text-lg px-6 py-3',
  };

  const disabledClasses = disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer';
  const widthClass = fullWidth ? 'w-full' : '';
  
  const buttonClasses = classNames(
    baseClasses,
    variantClasses[variant],
    sizeClasses[size],
    disabledClasses,
    widthClass,
    className
  );

  return (
    <motion.button
      whileHover={!disabled ? "hover" : undefined}
      whileTap={!disabled ? "tap" : undefined}
      variants={buttonAnimation}
      className={buttonClasses}
      onClick={onClick}
      disabled={disabled}
      type={type}
    >
      {icon && iconPosition === 'left' && (
        <span className="mr-2">{icon}</span>
      )}
      {children}
      {icon && iconPosition === 'right' && (
        <span className="ml-2">{icon}</span>
      )}
    </motion.button>
  );
};

export default Button; 