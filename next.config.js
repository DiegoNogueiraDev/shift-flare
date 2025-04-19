/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  images: {
    domains: ['images.unsplash.com'],
  },
  output: 'export',  // Para exportação estática que será servida pelo Spring Boot
  trailingSlash: true, // Importante para a exportação estática
}

module.exports = nextConfig 