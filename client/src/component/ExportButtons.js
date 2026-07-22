import React from 'react';

const apiServer = process.env.REACT_APP_API_SERVER;

const ExportButtons = () => {

  const downloadFile = (url, fileName) => {
    fetch(url, {
      method: 'GET',
    })
    .then(response => response.blob())
    .then(blob => {
      // Create a Blob object from the response data
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName;
      document.body.appendChild(a);

      // Trigger a click event on the anchor element to initiate the download
      a.click();

      // Remove the temporary anchor element
      document.body.removeChild(a);

      // Release the object URL
      window.URL.revokeObjectURL(url);
    })
    .catch(error => console.error('Error:', error));
  };

  const handleExportExcel = () => {
    downloadFile(`${apiServer}/rooms/api/excel`, 'room_data.xlsx');
  };

  const handleExportPDF = () => {
    downloadFile(`${apiServer}/rooms/api/pdf`, 'room_data.pdf');
  };

  return (
    <div>
      <button className="btn btn-primary mr-4" onClick={handleExportExcel}>Import to Excel</button>
      <button className="btn btn-danger" onClick={handleExportPDF}>Import to PDF</button>
    </div>
  );
};

export default ExportButtons;
