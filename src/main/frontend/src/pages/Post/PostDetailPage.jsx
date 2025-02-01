import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/PostStyles.css';

const PostDetailPage = () => {
  const { id } = useParams();
  const [post, setPost] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await api.get(`/posts/${id}`);
        setPost(response.data);
      } catch (error) {
        alert('게시물 조회 실패');
      }
    };
    fetchPost();
  }, [id]);

  const handleDelete = async () => {
    if (window.confirm('게시글을 삭제하겠습니까?')) {
      await api.delete(`/posts/${id}`);
      alert('삭제완료');
      navigate('/posts');
    }
  };

  return (
    <div className="post-container">
      {post ? (
        <>
          <h2 className="post-title">{post.title}</h2>
          <p className="post-author">작성자: {post.author}</p>
          <p className="post-content">{post.content}</p>
          <button
            className="post-btn"
            onClick={() => navigate(`/posts/edit/${id}`)}
          >
            수정
          </button>
          <button className="post-btn delete-btn" onClick={handleDelete}>
            삭제
          </button>
        </>
      ) : (
        <p>로딩 중...</p>
      )}
    </div>
  );
};

export default PostDetailPage;
