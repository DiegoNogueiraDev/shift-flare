"use client";

import React from 'react';
import { motion } from 'framer-motion';
import { cardAnimation } from '@/utils/animations';
import classNames from 'classnames';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  onClick?: () => void;
  animated?: boolean;
  hoverable?: boolean;
}

const Card: React.FC<CardProps> = ({
  children,
  className = '',
  onClick,
  animated = true,
  hoverable = true,
}) => {
  const baseClasses = 'bg-white rounded-lg shadow-card p-6';
  const hoverClasses = hoverable ? 'transition-all duration-300 hover:shadow-lg' : '';
  
  const cardClasses = classNames(
    baseClasses,
    hoverClasses,
    className
  );

  if (animated) {
    return (
      <motion.div
        className={cardClasses}
        onClick={onClick}
        variants={cardAnimation}
        initial="hidden"
        whileInView="visible"
        whileHover={hoverable ? "hover" : undefined}
        viewport={{ once: true, amount: 0.2 }}
      >
        {children}
      </motion.div>
    );
  }

  return (
    <div className={cardClasses} onClick={onClick}>
      {children}
    </div>
  );
};

export default Card; 