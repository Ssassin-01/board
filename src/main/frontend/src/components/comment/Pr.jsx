import React, { useEffect, useState } from 'react';
import api from '../../api/axiosInstance';

const Pr = ({ postId, onClose }) => {
  const [comments, setComments] = useState([]);

  useEffect(() => {
    const fetchComments = async () => {
      try {
        const response = await api.get(`/comments/${postId}`);
        setComments(response.data);
      } catch (error) {
        alert(
          error.response?.data?.message || '댓글 불러오기에 실패하였습니다.'
        );
      }
    };
    fetchComments();
  }, [postId]);

  return (
    <div>
      <div className="header">
        <img src="" alt="" />
      </div>
    </div>
  );
};

export default Pr;
