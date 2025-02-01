import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/PostStyles.css';

const PostCreatePage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/posts/create', formData);
      alert('작성하기 성공');
      navigate('/posts');
    } catch (error) {
      alert('게시글 작성 실패');
    }
  };

  return (
    <div className="post-container">
      <h2 className="post-title">새 게시글 작성</h2>
      <form className="post-form" onSubmit={handleSubmit}>
        <input
          className="post-input"
          type="text"
          name="title"
          placeholder="제목"
          onChange={handleChange}
        />
        <textarea
          className="post-input"
          name="content"
          placeholder="내용"
          onChange={handleChange}
        />
        <button className="post-btn" type="submit">
          작성
        </button>
      </form>
    </div>
  );
};

export default PostCreatePage;
