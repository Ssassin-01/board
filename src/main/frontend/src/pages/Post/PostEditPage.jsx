import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/PostStyles.css';

const PostEditPage = () => {
  const { id } = useParams();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
  });
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await api.get(`/posts/${id}`);
        setFormData(response.data);
      } catch (error) {
        alert('게시글 정보를 불러오는데 실패하였습니다.');
      }
    };
    fetchPost();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/posts/${id}`, formData);
      alert('수정완료!');
      navigate(`/`);
    } catch (error) {
      alert('게시글 수정 실패');
    }
  };

  return (
    <div className="post-container">
      <h2 className="post-title">게시글 수정</h2>
      <form className="post-form" onSubmit={handleSubmit}>
        <input
          className="post-input"
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
        />
        <textarea
          className="post-input"
          name="content"
          value={formData.content}
          onChange={handleChange}
        />
        <button className="post-btn" type="submit">
          수정 완료
        </button>
      </form>
    </div>
  );
};

export default PostEditPage;
