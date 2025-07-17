import React from 'react';

const Loader: React.FC = () => {
  return (
    <div className="flex items-center justify-center h-full">
      <div className="w-12 h-12 border-4 border-gray-700 border-t-blue-500 rounded-full animate-spin" />
    </div>
  );
};

export default Loader;
